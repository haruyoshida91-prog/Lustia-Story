package com.lustia.story.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.lustia.story.R
import com.lustia.story.databinding.FragmentGameBinding
import com.lustia.story.engine.StoryEngine
import com.lustia.story.data.GameState
import com.lustia.story.data.Story
import com.lustia.story.repository.StoryRepository

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var gameState: GameState? = null
    private var storyEngine: StoryEngine? = null
    private val storyRepository by lazy { StoryRepository() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val story = storyRepository.loadStory()
        if (story != null) {
            storyEngine = StoryEngine(story)
            gameState = GameState(
                currentSceneId = story.startSceneId,
                playerStats = mutableMapOf(
                    "health" to 100,
                    "strength" to 15,
                    "defense" to 10,
                    "charisma" to 12
                ),
                inventory = mutableListOf(),
                visitedScenes = mutableSetOf(story.startSceneId)
            )
            displayCurrentScene()
        }
    }

    private fun displayCurrentScene() {
        if (gameState == null || storyEngine == null) return

        val scene = storyEngine!!.getCurrentScene(gameState!!) ?: return
        binding.sceneText.text = scene.text

        val availableChoices = storyEngine!!.getAvailableChoices(gameState!!)
        displayChoices(availableChoices)
    }

    private fun displayChoices(choices: List<com.lustia.story.data.Choice>) {
        binding.choicesContainer.removeAllViews()

        choices.forEach { choice ->
            val button = Button(requireContext()).apply {
                text = choice.text
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8
                }
                setOnClickListener {
                    handleChoiceSelected(choice)
                }
            }
            binding.choicesContainer.addView(button)
        }
    }

    private fun handleChoiceSelected(choice: com.lustia.story.data.Choice) {
        if (gameState != null && storyEngine != null) {
            storyEngine!!.moveToScene(choice.nextSceneId, gameState!!)
            displayCurrentScene()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
