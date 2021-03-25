package com.andry.address.oriontek.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.andry.address.oriontek.fragments.ExistingUserFragment;
import com.andry.address.oriontek.fragments.NewUserFragment;

public class SectionsPageAdapterLogin extends FragmentStateAdapter {


    public SectionsPageAdapterLogin(@NonNull  FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Provide a new Fragment associated with the specified position.
     * <p>
     * The adapter will be responsible for the Fragment lifecycle:
     * <ul>
     *     <li>The Fragment will be used to display an item.</li>
     *     <li>The Fragment will be destroyed when it gets too far from the viewport, and its state
     *     will be saved. When the item is close to the viewport again, a new Fragment will be
     *     requested, and a previously saved state will be used to initialize it.
     * </ul>
     *
     * @param position
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        if (position == 0)
            fragment = new ExistingUserFragment();
        else if (position ==1)
            fragment = new NewUserFragment();

        return fragment;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
