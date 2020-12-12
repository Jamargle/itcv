package com.jmlb0003.itcv.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Allows a parent fragment to create child fragments by using the assigned fragment factory
 */
internal inline fun <reified T : Fragment> Fragment.createChildFragment(): T =
    childFragmentManager.fragmentFactory.instantiate(
        checkNotNull(T::class.java.classLoader),
        T::class.java.name
    ) as? T
        ?: throw ClassCastException("This Fragment factory cannot create Fragments of type ${T::class.java.name}")

/**
 * Allows an activity to create child fragments by using the assigned fragment factory
 */
internal inline fun <reified T : Fragment> FragmentActivity.createFragment(): T =
    supportFragmentManager.fragmentFactory.instantiate(
        checkNotNull(T::class.java.classLoader),
        T::class.java.name
    ) as? T
        ?: throw ClassCastException("This Fragment factory cannot create Fragments of type ${T::class.java.name}")
