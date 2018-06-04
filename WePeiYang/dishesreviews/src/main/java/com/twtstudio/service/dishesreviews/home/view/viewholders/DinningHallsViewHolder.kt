package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.extensions.DishPreferences
import com.twtstudio.service.dishesreviews.home.view.adapters.DinningHallsAdapter

/**
 * Created by zhangyulong on 18-3-23.
 */
class DinningHallsViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_dinning_halls)
    private val spCampus = itemView.findViewById<Spinner>(R.id.sp_campus)
    private val dinningHallsAdapter = DinningHallsAdapter(emptyList(), itemView.context, lifecycleOwner)
    private val NEW_CAMPUS = "北洋园校区"
    private val OLD_CAMPUS = "卫津路校区"
    override fun bind() {
        recyclerView.apply() {
            layoutManager = GridLayoutManager(itemView.context, 4)
            adapter = dinningHallsAdapter
        }
        spCampus.apply {
            adapter = ArrayAdapter.createFromResource(itemView.context,
                    R.array.campus_array, android.R.layout.simple_spinner_item)
                    .apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                when (p0?.getItemAtPosition(p2)) {
                                    NEW_CAMPUS -> DishPreferences.isNewCampus = true
                                    OLD_CAMPUS -> DishPreferences.isNewCampus = false
                                }
                                dinningHallsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
            if (DishPreferences.isNewCampus)
                setSelection((adapter as ArrayAdapter<CharSequence>?)!!.getPosition(NEW_CAMPUS))
            else
                setSelection((adapter as ArrayAdapter<CharSequence>?)!!.getPosition(OLD_CAMPUS))
        }
    }
}