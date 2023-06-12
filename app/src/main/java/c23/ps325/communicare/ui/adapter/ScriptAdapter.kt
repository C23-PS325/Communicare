package c23.ps325.communicare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import c23.ps325.communicare.databinding.ItemScriptBinding
import c23.ps325.communicare.model.DataItem
import c23.ps325.communicare.model.TextScript

class ScriptAdapter: RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder>() {

//    private val inflater : LayoutInflater = LayoutInflater.from(ctx)
    private val differCallback = object : DiffUtil.ItemCallback<TextScript>(){
        override fun areItemsTheSame(oldItem: TextScript, newItem: TextScript): Boolean {
            return oldItem.textScript == newItem.textScript
        }

        override fun areContentsTheSame(oldItem: TextScript, newItem: TextScript): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    class ScriptViewHolder(private val binding : ItemScriptBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: TextScript){
            binding.textScript.text = item.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        return ScriptViewHolder(
            ItemScriptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.setItem(item)
        holder.setIsRecyclable(false)
    }

    fun setData(data: ArrayList<TextScript>) = differ.submitList(data)
}