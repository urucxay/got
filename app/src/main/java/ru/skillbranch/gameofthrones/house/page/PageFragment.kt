package ru.skillbranch.gameofthrones.house.page

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_page.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.main.MainActivity

class PageFragment : Fragment() {

    private lateinit var pageAdapter: HousePageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()

        pageAdapter = HousePageAdapter(childFragmentManager)
        vp_houses.adapter = pageAdapter
        tl_houses.setupWithViewPager(vp_houses)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    private fun initToolbar() {
        (activity as MainActivity).registerToolbar(toolbar)
    }

}

