package c23.ps325.communicare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import c23.ps325.communicare.databinding.ItemScriptBinding

class ScriptAdapter: RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder>() {

//    private val inflater : LayoutInflater = LayoutInflater.from(ctx)
    private val differCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    class ScriptViewHolder(private val binding : ItemScriptBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: String){
            binding.textScript.text = item
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

    fun setData(data: List<String>) = differ.submitList(data)
}