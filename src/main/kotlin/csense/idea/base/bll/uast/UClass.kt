package csense.idea.base.bll.uast

import com.intellij.codeInsight.ExternalAnnotationsManager
import csense.idea.base.annotationss.resolveAllClassAnnotations
import csense.idea.base.mpp.MppAnnotation
import csense.idea.base.mpp.resolveAllClassMppAnnotation
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElementOfType

fun UClass.computeSuperAnnotations(extManager: ExternalAnnotationsManager): List<UAnnotation> {
    val annotations = mutableListOf<UAnnotation>()
    var currentSuper = javaPsi.superClass?.toUElementOfType<UClass>()
    while (currentSuper != null) {
        annotations += currentSuper.resolveAllClassAnnotations(extManager)
        currentSuper = currentSuper.javaPsi.superClass?.toUElementOfType()
    }
    return annotations
}


fun UClass.computeSuperMppAnnotations(extManager: ExternalAnnotationsManager): List<MppAnnotation> {
    val annotations = mutableListOf<MppAnnotation>()
    var currentSuper = javaPsi.superClass
    while (currentSuper != null) {
        annotations += currentSuper.resolveAllClassMppAnnotation(extManager)
        currentSuper = currentSuper.superClass
    }
    return annotations
}


fun UClass.isSubTypeOf(other: UClass) = isChildOfSafe(other)

fun UClass.isChildOfSafe(other: UClass): Boolean {
    var currentClass: UClass? = this
    while (currentClass != null) {
        if (currentClass == other) {
            return true
        }
        currentClass = currentClass.javaPsi.superClass?.toUElementOfType()
    }
    return false
}


