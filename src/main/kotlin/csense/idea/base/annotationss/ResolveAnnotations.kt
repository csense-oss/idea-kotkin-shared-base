package csense.idea.base.annotationss

import com.intellij.codeInsight.ExternalAnnotationsManager
import com.intellij.psi.*
import csense.idea.base.UastKtPsi.resolvePsi
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.psi.*
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.toUElementOfType

fun KtExpression.resolveAnnotationsKt(extManager: ExternalAnnotationsManager): List<UAnnotation> = when (this) {
    is KtCallExpression -> resolvePsi()?.resolveAllMethodAnnotations(extManager)
    is KtProperty -> (getter ?: initializer)?.resolveAllMethodAnnotations(extManager)?.plus(uAnnotaions())
    is KtDotQualifiedExpression -> selectorExpression?.resolveAnnotationsKt(extManager)
    is KtNameReferenceExpression -> this.references.firstOrNull()?.resolve()?.resolveAnnotations(extManager)
    is KtNamedFunction -> resolveAllMethodAnnotations()
    else -> emptyList()
} ?: emptyList()

fun PsiElement.resolveAnnotations(extManager: ExternalAnnotationsManager): List<UAnnotation> {
    return when (this) {
        is KtExpression -> resolveAnnotationsKt(extManager)
        is PsiMethod -> resolveAllMethodAnnotations(extManager)
        is PsiField -> this.uAnnotations()
        else -> emptyList()
    }
}


fun PsiElement.resolveAllMethodAnnotations(externalAnnotationsManager: ExternalAnnotationsManager? = null): List<UAnnotation> {
    val extManager = externalAnnotationsManager ?: ExternalAnnotationsManager.getInstance(project)
    val ownAnnotations = when (this) {
        is KtLightMethod -> annotations.mapNotNull { it.toUElementOfType<UAnnotation>() }
        is KtFunction -> annotationEntries.mapNotNull { it.toUElementOfType<UAnnotation>() }
        is PsiMethod -> annotations.mapNotNull { it.toUElementOfType<UAnnotation>() }
        else -> emptyList()
    }
    val external = if (this is PsiModifierListOwner) {
        extManager.findExternalAnnotations(this)?.mapNotNull {
            it.toUElementOfType<UAnnotation>()
        } ?: emptyList()
    } else {
        emptyList()
    }
    return ownAnnotations + external
}

fun PsiElement.resolveAllClassAnnotations(externalAnnotationsManager: ExternalAnnotationsManager? = null): List<UAnnotation> {
    val extManager = externalAnnotationsManager ?: ExternalAnnotationsManager.getInstance(project)
    val internal = when (this) {
        is KtLightClass -> annotations.mapNotNull { it.toUElementOfType<UAnnotation>() }
        is KtClass -> annotationEntries.mapNotNull { it.toUElementOfType<UAnnotation>() }
        is KtClassOrObject -> annotationEntries.mapNotNull { it.toUElementOfType<UAnnotation>() }
        else -> emptyList()
    }
    val external = if (this is PsiModifierListOwner) {
        extManager.findExternalAnnotations(this)?.mapNotNull {
            it.toUElementOfType<UAnnotation>()
        } ?: emptyList()
    } else {
        emptyList()
    }
    return internal + external
}

fun PsiElement.resolveAllParameterAnnotations(externalAnnotationsManager: ExternalAnnotationsManager? = null): List<List<UAnnotation>> {
    val extManager = externalAnnotationsManager ?: ExternalAnnotationsManager.getInstance(project)
    return when (this) {
        is KtLightMethod -> parameterList.getAllAnnotations(extManager)
        is KtFunction -> valueParameters.getAllAnnotations(extManager)
        is PsiMethod -> parameterList.getAllAnnotations(extManager)
        else -> emptyList()
    }
}

fun List<KtParameter>.getAllAnnotations(
    extManager: ExternalAnnotationsManager
): List<List<UAnnotation>> = map {
    it.annotationEntries.toUAnnotation(extManager)
}

fun List<KtAnnotationEntry>.toUAnnotation(extManager: ExternalAnnotationsManager) = mapNotNull { it.toUElementOfType<UAnnotation>() }

fun PsiParameterList.getAllAnnotations(extManager: ExternalAnnotationsManager): List<List<UAnnotation>> {
    val internal = parameters.map {
        it.annotations.mapNotNull { it.toUElementOfType<UAnnotation>() }
    }
    val external = parameters.map {
        extManager.findExternalAnnotations(it)?.mapNotNull { it.toUElementOfType<UAnnotation>() }
            ?: emptyList()
    }
    return internal + external
}
