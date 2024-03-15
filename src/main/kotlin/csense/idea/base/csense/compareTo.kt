@file:Suppress("RedundantVisibilityModifier", "NOTHING_TO_INLINE")

package csense.idea.base.csense

public inline operator fun Number.compareTo(other: Number): Int = when (this) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    else -> toDouble().compareTo(other)
}

public inline operator fun Byte.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}

public inline operator fun Short.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}

public inline operator fun Int.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}

public inline operator fun Long.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}

public inline operator fun Float.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}

public inline operator fun Double.compareTo(other: Number): Int = when (other) {
    is Byte -> this.compareTo(other)
    is Short -> this.compareTo(other)
    is Int -> this.compareTo(other)
    is Long -> this.compareTo(other)
    is Float -> this.compareTo(other)
    is Double -> this.compareTo(other)
    //TODO bad fallback? hmm
    else -> this.compareTo(other.toDouble())
}
