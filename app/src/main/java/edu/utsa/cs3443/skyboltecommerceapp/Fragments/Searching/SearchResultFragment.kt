package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Searching

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.SearchResultViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentSearchResultsBinding

class SearchResultFragment : Fragment()
{
    private lateinit var binding: FragmentSearchResultsBinding
    val viewModel by viewModels<SearchResultViewModel>()



}