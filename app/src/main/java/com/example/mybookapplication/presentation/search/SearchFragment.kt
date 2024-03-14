package com.example.mybookapplication.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.presentation.search.adapter.GenreAdapter

class SearchFragment : Fragment() {
    private lateinit var adapter: GenreAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        setUpRecyclerView(view)

        return view
    }
    private fun setUpRecyclerView(view : View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.genreList)
        val genres : List<String> = listOf("Детектив", "Боевик", "Классика", "Психология", "Фэнтези", "История", "Детские", "Компьютеры")
        adapter = GenreAdapter(genres)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}