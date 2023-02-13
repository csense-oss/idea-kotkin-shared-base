@file:Suppress("unused")

package csense.idea.base.settings.propertiesComponent

import com.intellij.ide.util.*
import kotlin.reflect.*


class IntSetting(
    private val backend: PropertiesComponent,
    private val settingsNamePrefix: String,
    private val postfixName: String = "",
    private val defaultValue: Int = 0,
    private val minValue: Int = Int.MIN_VALUE,
    private val maxValue: Int = Int.MAX_VALUE
) {
    operator fun getValue(prop: Any, property: KProperty<*>): Int {
        return backend.getInt(
            /* name = */ settingsNamePrefix + property.name + postfixName,
            /* defaultValue = */
            defaultValue
        )
    }

    operator fun setValue(prop: Any, property: KProperty<*>, newValue: Int) {
        val safeValue: Int = newValue.coerceAtLeast(minValue).coerceAtMost(maxValue)
        backend.setValue(
            /* p0 = */ settingsNamePrefix + property.name + postfixName,
            /* p1 = */ safeValue,
            /* p2 = */ defaultValue
        )
    }
}
