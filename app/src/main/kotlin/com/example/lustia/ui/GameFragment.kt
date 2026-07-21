package com.example.lustia.ui

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lustia.story.StoryRepository
import com.example.lustia.story.StoryNode
import com.example.lustia.story.StoryOption
import com.example.lustia.story.StoryAction
import com.example.lustia.player.PlayerCharacter
import com.example.lustia.player.Race
import com.example.lustia.save.SaveManager

class GameFragment : Fragment() {

    private val storyRepo = StoryRepository()
    private var player = PlayerCharacter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Build a simple vertical layout programmatically
        val root = ScrollView(requireContext())
        val containerLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }
        root.addView(containerLayout)

        val titleView = TextView(requireContext()).apply { textSize = 20f }
        val contentView = TextView(requireContext()).apply { textSize = 16f }
        val optionsContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Character customization UI
        val raceSpinner = Spinner(requireContext())
        val races = Race.values().map { it.name }
        raceSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, races)
        val heightInput = EditText(requireContext()).apply {
            hint = "Height (cm)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            filters = arrayOf(InputFilter.LengthFilter(3))
        }
        val saveButton = Button(requireContext()).apply { text = "Save Character" }
        saveButton.setOnClickListener {
            val selected = Race.values()[raceSpinner.selectedItemPosition]
            val (min, max) = when (selected) {
                Race.HUMAN -> 140 to 220
                Race.ELF -> 150 to 210
                Race.DWARF -> 100 to 140
            }
            val h = heightInput.text.toString().toIntOrNull()
            if (h == null || h < min || h > max) {
                Toast.makeText(requireContext(), "Height must be between $min and $max for $selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            player.race = selected
            player.heightCm = h
            SaveManager.savePlayer(requireContext(), player)
            Toast.makeText(requireContext(), "Saved character", Toast.LENGTH_SHORT).show()
        }

        containerLayout.addView(titleView)
        containerLayout.addView(contentView)
        containerLayout.addView(optionsContainer)
        containerLayout.addView(TextView(requireContext()).apply { text = "\nCharacter Customization" })
        containerLayout.addView(raceSpinner)
        containerLayout.addView(heightInput)
        containerLayout.addView(saveButton)

        // Load saved player if any
        SaveManager.loadPlayer(requireContext())?.let { loaded ->
            player = loaded
            // reflect in UI
            raceSpinner.setSelection(Race.values().indexOf(player.race))
            heightInput.setText(player.heightCm.toString())
        }

        // Show start node
        val start = storyRepo.getNode("start")
        if (start != null) showNode(start, titleView, contentView, optionsContainer)

        return root
    }

    private fun showNode(node: StoryNode, titleView: TextView, contentView: TextView, optionsContainer: LinearLayout) {
        titleView.text = node.title
        contentView.text = node.content
        optionsContainer.removeAllViews()

        node.options.forEach { option ->
            val btn = Button(requireContext())
            btn.text = option.text
            btn.setOnClickListener {
                // execute actions
                option.actions.forEach { action ->
                    when (action) {
                        is StoryAction.StartCombat -> {
                            Toast.makeText(requireContext(), "Start combat: ${action.enemyId}", Toast.LENGTH_SHORT).show()
                            // TODO: integrate CombatEngine; for now navigate to next node if present
                        }
                        is StoryAction.GiveItem -> {
                            Toast.makeText(requireContext(), "Received item: ${action.itemId}", Toast.LENGTH_SHORT).show()
                        }
                        is StoryAction.ModifyStat -> {
                            when (action.stat.lowercase()) {
                                "hp" -> player.hp = (player.hp + action.amount).coerceAtMost(player.maxHp)
                                "lust" -> player.lust = (player.lust + action.amount).coerceAtMost(player.maxLust)
                            }
                        }
                        StoryAction.None -> {}
                    }
                }
                option.nextNodeId?.let { nextId ->
                    storyRepo.getNode(nextId)?.let { nextNode ->
                        showNode(nextNode, titleView, contentView, optionsContainer)
                    }
                }
            }
            optionsContainer.addView(btn)
        }
    }
}
