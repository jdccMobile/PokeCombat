package com.jdccmobile.pokecombat.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import com.jdccmobile.pokecombat.R
import com.jdccmobile.pokecombat.databinding.FragmentSelectedPokemonBinding
import com.jdccmobile.pokecombat.ui.viewModel.SelectedPokemonViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val MY_POKEMON_ID = "MY_POKEMON_ID"

@AndroidEntryPoint
class SelectedPokemonFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentSelectedPokemonBinding? = null
    private val binding get() = _binding!!

    private val selectedPokemonViewModel: SelectedPokemonViewModel by viewModels()

    private var pokemonId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonId = it.getInt(POKEMON_ID)
        }
        if (pokemonId != null) selectedPokemonViewModel.getPokemonInfo(pokemonId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedPokemonViewModel.pokemonInfo.observe(requireActivity()) { pokemonInfo ->
            binding.tvSelectedPkmName.text = pokemonInfo.name.replaceFirstChar { it.uppercase() }
            binding.tvSelectedPkmId.text = String.format("#%03d", pokemonInfo.id)
            binding.tvSelectedPkmAttackStats.text = pokemonInfo.stats[1].base_stat.toString()
            binding.tvSelectedPkmHPStats.text = pokemonInfo.stats[0].base_stat.toString()
            binding.tvSelectedPkmSpeedStats.text = pokemonInfo.stats[5].base_stat.toString()
            val url =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonInfo.id}.png"
            Picasso.get().load(url).into(binding.ivSelectedPkmImage)
            binding.tvSelectedPkmDifStat.text = getPokemonDifficulty(
                pokemonInfo.stats[1].base_stat,
                pokemonInfo.stats[0].base_stat
            ).replaceFirstChar { it.uppercase() }
        }
        initListeners()
    }


    private fun initListeners() {
        binding.vBackground.setOnClickListener { } // To block the background
        binding.ivSelectPkmCancel.setOnClickListener {
            requireActivity().findViewById<FragmentContainerView>(R.id.frSelectedPokemonContainer).visibility = View.GONE
            parentFragmentManager.popBackStack()
        }
        binding.btSelectPkm.setOnClickListener {
            val intent = Intent(requireActivity(), CombatActivity::class.java)
            intent.putExtra(MY_POKEMON_ID, pokemonId)
            startActivity(intent)
            requireActivity().findViewById<FragmentContainerView>(R.id.frSelectedPokemonContainer).visibility = View.GONE
            parentFragmentManager.popBackStack()
        }
    }

    private fun getPokemonDifficulty(attack: Int, HP: Int): String {
        return when (attack + HP) {
            in 0..80 -> "Muy dificil"
            in 81..120 -> "Díficil"
            in 121..155 -> "Normal"
            in 156..250 -> "Fácil"
            else -> "Muy fácil"
        }
    }

    companion object {
        const val POKEMON_ID = "pokemonId"

        fun newInstance() =
            SelectedPokemonFragment().apply {
                arguments = Bundle().apply {
                    putInt(POKEMON_ID, pokemonId!!)
                }
            }
    }
}