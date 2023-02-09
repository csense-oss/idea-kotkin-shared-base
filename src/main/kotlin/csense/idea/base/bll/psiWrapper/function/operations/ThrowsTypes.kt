@file:Suppress("unused")

package csense.idea.base.bll.psiWrapper.function.operations

import com.intellij.psi.*
import csense.idea.base.bll.kotlin.*
import csense.idea.base.bll.psi.*
import csense.idea.base.bll.psiWrapper.`class`.*
import csense.idea.base.bll.psiWrapper.`class`.operations.*
import csense.idea.base.bll.psiWrapper.function.*
import csense.kotlin.extensions.collections.*
import org.jetbrains.kotlin.psi.*


fun KtPsiFunction.throwsTypes(): List<KtPsiClass> = when (this) {
    is KtPsiFunction.Kt -> function.throwsTypes()
    is KtPsiFunction.Psi -> function.throwsTypes()
}

fun KtFunction.throwsTypes(): List<KtPsiClass> {
    val throws: List<KtAnnotationEntry> = annotationEntries.filter { it.isThrowsAnnotation() }
    if (throws.isEmpty()) {
        return emptyList()
    }
    //TODO varargs!?... :(
    return throws.map {
        it.valueArguments.mapNotNull { annotation ->
            annotation.getArgumentExpression()?.resolveFirstClassType()?.asKtOrPsiClass()
        }
    }.flatten().nullOnEmpty() ?: listOfNotNull(
        //TODO cache etc.. this is horrible..
        KtPsiClass.resolve("kotlin.Throwable", project)
    )
}

fun PsiMethod.throwsTypes(): List<KtPsiClass> {
    val throws: List<PsiAnnotation> = annotations.filter { it.isThrowsAnnotation() }
    //TODO varargs!?... :(
    return throws.mapNotNull { it.resolveFirstClassType()?.asKtOrPsiClass() }
}

//TODO move and fix
fun PsiAnnotation.isThrowsAnnotation(): Boolean {
    return qualifiedName == "Throws"
}

//TODO move?..
fun KtAnnotationEntry.isThrowsAnnotation(): Boolean {
    return shortName?.asString() == "Throws"
}
