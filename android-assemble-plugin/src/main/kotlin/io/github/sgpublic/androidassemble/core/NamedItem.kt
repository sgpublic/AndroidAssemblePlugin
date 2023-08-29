package io.github.sgpublic.androidassemble.core

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

/**
 * @author Madray Haven
 * @Date 2023/8/8 14:11
 */
fun <T> NamedDomainObjectContainer<T>.register(
    namedItem: Named, block: (T.() -> Unit)? = null
): NamedDomainObjectProvider<T> {
    return register(namedItem.name) {
        block?.invoke(this)
    }
}

fun <T> NamedDomainObjectContainer<T>.create(
    namedItem: Named, block: (T.() -> Unit)? = null
): T {
    return create(namedItem.name) {
        block?.invoke(this)
    }
}

fun <T> NamedDomainObjectContainer<T>.maybeCreate(
    namedItem: Named
): T {
    return maybeCreate(namedItem.name)
}

fun <T> NamedDomainObjectCollection<T>.named(
    namedItem: Named, block: (T.() -> Unit)? = null
): NamedDomainObjectProvider<T> {
    return named(namedItem.name) {
        block?.invoke(this)
    }
}

fun <S: T, T> NamedDomainObjectCollection<T>.named(
    namedItem: Named, clazz: Class<S>, block: (S.() -> Unit)? = null
): NamedDomainObjectProvider<S> {
    return named(namedItem.name, clazz) {
        block?.invoke(this)
    }
}
