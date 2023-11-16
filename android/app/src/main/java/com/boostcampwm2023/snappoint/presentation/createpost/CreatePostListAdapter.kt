package com.boostcampwm2023.snappoint.presentation.createpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampwm2023.snappoint.databinding.ItemImageBlockBinding
import com.boostcampwm2023.snappoint.databinding.ItemTextBlockBinding

class CreatePostListAdapter(
    private val listener: (Int, String) -> Unit,
    private val onDeleteButtonClicked: (Int) -> Unit
) : RecyclerView.Adapter<BlockItemViewHolder>() {

    private var blocks: MutableList<PostBlock> = mutableListOf()

    fun getCurrentBlocks() = blocks.toList()

    fun updateBlocks(newBlocks: List<PostBlock>) {
        blocks = newBlocks.toMutableList()
    }

    private fun deleteBlocks(position: Int) {
        blocks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, blocks.size - position)
    }

    override fun getItemViewType(position: Int): Int {
        return when(blocks[position]) {
            is PostBlock.STRING -> ViewType.STRING.ordinal
            is PostBlock.IMAGE -> ViewType.IMAGE.ordinal
            is PostBlock.VIDEO -> ViewType.VIDEO.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ViewType.IMAGE.ordinal -> {
                return BlockItemViewHolder.ImageBlockViewHolder(
                    ItemImageBlockBinding.inflate(inflater, parent, false),
                    listener
                ) { index ->
                    onDeleteButtonClicked(index)
                    deleteBlocks(index)
                }
            }

            ViewType.VIDEO.ordinal -> {
                TODO()
            }
        }
        return BlockItemViewHolder.TextBlockViewHolder(
            ItemTextBlockBinding.inflate(inflater, parent, false),
            listener
        ) { index ->
            onDeleteButtonClicked(index)
            deleteBlocks(index)
        }
    }

    override fun getItemCount(): Int {
        return blocks.size
    }

    override fun onBindViewHolder(holder: BlockItemViewHolder, position: Int) {
        when (holder) {
            is BlockItemViewHolder.TextBlockViewHolder -> holder.bind(blocks[position].content, position)
            is BlockItemViewHolder.ImageBlockViewHolder -> holder.bind(blocks[position].content, (blocks[position] as PostBlock.IMAGE).uri, position)
        }
    }

    override fun onViewAttachedToWindow(holder: BlockItemViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attachTextWatcherToEditText()
    }

    override fun onViewDetachedFromWindow(holder: BlockItemViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachTextWatcherFromEditText()
    }
}

@BindingAdapter("blocks", "listener", "onDeleteButtonClick")
fun RecyclerView.bindRecyclerViewAdapter(blocks: List<PostBlock>, listener: (Int, String) -> Unit, onDeleteButtonClicked: (Int) -> Unit) {
    if (adapter == null) adapter = CreatePostListAdapter(listener, onDeleteButtonClicked)

    when {
        // 아이템 추가
        (adapter as CreatePostListAdapter).getCurrentBlocks().size < blocks.size -> {
            with(adapter as CreatePostListAdapter) {
                updateBlocks(blocks)
                notifyItemInserted(blocks.size - 1)
            }
        }

        // content 변경
        (adapter as CreatePostListAdapter).getCurrentBlocks().size == blocks.size -> {
            with(adapter as CreatePostListAdapter) {
                updateBlocks(blocks)
            }
        }
    }
}