package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * An adapter fro the Viewpager, so users can examine products at their leisure
 *
 * @param FragmentManager Manages the fragments that the user will see
 * @param Lifecycle Manages the lifecycle of the fragments, typically will last until we exit the page, or when we exit the app
 * @param List Private, these are the categories we will be using
 */

class HomeViewpagerAdapter(
    private val fragments: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int
    {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment
    {
        return fragments[position]
    }

}