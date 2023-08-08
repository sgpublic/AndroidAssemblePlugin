package io.github.sgpublic.androidassemble.core

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

/**
 * @author Madray Haven
 * @Date 2023/8/8 14:11
 */
interface NamedItem {
    val pubName: String
}

fun <T> NamedDomainObjectContainer<T>.register(
    namedItem: NamedItem, block: (T.() -> Unit)? = null
): NamedDomainObjectProvider<T> {
    return register(namedItem.pubName) {
        block?.invoke(this)
    }
}

fun <T> NamedDomainObjectContainer<T>.create(
    namedItem: NamedItem, block: (T.() -> Unit)? = null
): T {
    return create(namedItem.pubName) {
        block?.invoke(this)
    }
}

fun <T> NamedDomainObjectContainer<T>.maybeCreate(
    namedItem: NamedItem
): T {
    return maybeCreate(namedItem.pubName)
}

fun <T> NamedDomainObjectCollection<T>.named(
    namedItem: NamedItem, block: (T.() -> Unit)? = null
): NamedDomainObjectProvider<T> {
    return named(namedItem.pubName) {
        block?.invoke(this)
    }
}

fun <S: T, T> NamedDomainObjectCollection<T>.named(
    namedItem: NamedItem, clazz: Class<S>, block: (S.() -> Unit)? = null
): NamedDomainObjectProvider<S> {
    return named(namedItem.pubName, clazz) {
        block?.invoke(this)
    }
}
