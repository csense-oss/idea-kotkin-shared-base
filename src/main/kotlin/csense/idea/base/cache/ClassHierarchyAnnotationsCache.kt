package csense.idea.base.cache

import com.intellij.codeInsight.ExternalAnnotationsManager
import com.intellij.psi.PsiClass
import csense.idea.base.bll.uast.computeSuperMppAnnotations
import csense.idea.base.mpp.MppAnnotation
import csense.idea.base.mpp.resolveAllClassMppAnnotation
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElementOfType
import java.util.concurrent.ConcurrentHashMap

//TODO resolve all parents.
object ClassHierarchyAnnotationsCache {


    private val superTypeLookupUAst: ConcurrentHashMap<UClass, List<MppAnnotation>> =
        ConcurrentHashMap(500)
    private val superTypeAnnotationsQualifiedNamesLookupKt: ConcurrentHashMap<KtClassOrObject, List<MppAnnotation>> =
        ConcurrentHashMap(500)

    /**
     * Tries to resolve the given class's parents annotations..
     * @return List<UAnnotation>
     */
    fun getClassHierarchyAnnotations(
        clazz: UClass?,
        extManager: ExternalAnnotationsManager
    ): List<MppAnnotation> {
        if (clazz == null) {
            return emptyList()
        }
        val superClz = clazz.javaPsi.superClass?.toUElementOfType<UClass>()
            ?: return clazz.resolveAllClassMppAnnotation(extManager)
        val superCache = superTypeLookupUAst[superClz]
        val superAnnotations: List<MppAnnotation> = superCache
            ?: clazz.computeSuperMppAnnotations(extManager).also {
                superTypeLookupUAst[superClz] = it
            }

        val myAnnotations = clazz.resolveAllClassMppAnnotation(extManager)
        return myAnnotations + superAnnotations
    }

    fun getClassHierarchyAnnotations(
        clazz: PsiClass?,
        extManager: ExternalAnnotationsManager
    ): List<MppAnnotation> {
        return getClassHierarchyAnnotations(clazz?.toUElementOfType<UClass>(), extManager)
    }

    fun getClassHierarchyAnnotations(
        clazz: KtClassOrObject?,
        extManager: ExternalAnnotationsManager
    ): List<MppAnnotation> {
        if (clazz == null) {
            return emptyList()
        }
        val myAnnotations = clazz.resolveAllClassMppAnnotation(extManager)
        val superClz: KtClassOrObject = clazz.firstChild as? KtClassOrObject
            ?: return myAnnotations
        val superCache = superTypeAnnotationsQualifiedNamesLookupKt[superClz]
        val superAnnotations: List<MppAnnotation> = superCache
            ?: clazz.resolveAllClassMppAnnotation(extManager).also {
                superTypeAnnotationsQualifiedNamesLookupKt[superClz] = it
            }

        return myAnnotations + superAnnotations
    }


}