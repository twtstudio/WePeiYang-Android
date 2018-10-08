package com.twt.service.home.user

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull

/**
 * Created by rickygao on 2018/1/28.
 */
class UserAdapter(
        private val items: List<UserItem>,
        private val inflater: LayoutInflater,
        private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<UserAdapter.UserItemViewHolder>() {

    companion object {
        //        const val VIEW_TYPE_DIVIDER = -1
        const val VIEW_TYPE_AVATAR = 0
        const val VIEW_TYPE_INFO = 1
        const val VIEW_TYPE_ACTION = 2
    }

    sealed class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class AvatarItemViewHolder(itemView: View) : UserItemViewHolder(itemView) {
            private val avatarIv = itemView.findViewById<ImageView>(R.id.iv_avatar).apply {
                transitionName = "avatar"
            }
            private val usernameTv = itemView.findViewById<TextView>(R.id.tv_username)
            private val realnameTv = itemView.findViewById<TextView>(R.id.tv_realname)

            fun bind(avatar: String, username: String, realname: String, defaultAvatarRes: Int) {
                Glide.with(itemView.context).load(avatar).asBitmap().placeholder(defaultAvatarRes).into(avatarIv)
                usernameTv.text = username
                realnameTv.text = realname
            }
        }

        class InfoItemViewHolder(itemView: View) : UserItemViewHolder(itemView) {
            private val iconIv = itemView.findViewById<ImageView>(R.id.iv_icon)
            private val labelTv = itemView.findViewById<TextView>(R.id.tv_label)
            private val infoTv = itemView.findViewById<TextView>(R.id.tv_info)

            fun bind(iconRes: Int, label: String, action: () -> Unit) {
                iconIv.setImageResource(iconRes)
                labelTv.text = label
                itemView.setOnClickListener { action() }
            }

            fun update(info: String) {
                infoTv.text = info
            }
        }

        class ActionItemViewHolder(itemView: View) : UserItemViewHolder(itemView) {
            private val iconIv = itemView.findViewById<ImageView>(R.id.iv_icon)
            private val labelTv = itemView.findViewById<TextView>(R.id.tv_label)

            fun bind(iconRes: Int, label: String, action: () -> Unit) {
                iconIv.setImageResource(iconRes)
                labelTv.text = label
                itemView.setOnClickListener { action() }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder = when (viewType) {
        VIEW_TYPE_AVATAR -> UserItemViewHolder.AvatarItemViewHolder(inflater.inflate(R.layout.item_user_avatar, parent, false))
        VIEW_TYPE_INFO -> UserItemViewHolder.InfoItemViewHolder(inflater.inflate(R.layout.item_user_info, parent, false))
        VIEW_TYPE_ACTION -> UserItemViewHolder.ActionItemViewHolder(inflater.inflate(R.layout.item_user_action, parent, false))
        else -> null
    }!!

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val item = items[position]
        when {
            item is UserItem.AvatarItem && holder is UserItemViewHolder.AvatarItemViewHolder -> {
                item.avatarSrc.bindNonNull(lifecycleOwner) {
                    holder.bind(it.avatar, it.username, it.realname, item.defaultAvatarRes)
                }
            }

            item is UserItem.InfoItem && holder is UserItemViewHolder.InfoItemViewHolder -> {
                holder.bind(item.iconRes, item.label, item.action)
                item.infoSrc.bindNonNull(lifecycleOwner) {
                    holder.update(it)
                }
            }

            item is UserItem.ActionItem && holder is UserItemViewHolder.ActionItemViewHolder -> {
                holder.bind(item.iconRes, item.label, item.action)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
//        is UserItem.DividerItem -> VIEW_TYPE_DIVIDER
        is UserItem.AvatarItem -> VIEW_TYPE_AVATAR
        is UserItem.InfoItem -> VIEW_TYPE_INFO
        is UserItem.ActionItem -> VIEW_TYPE_ACTION
    }
}

sealed class UserItem {

    data class AvatarBean(val avatar: String, val username: String, val realname: String)

    //    object DividerItem: UserItem()
    class AvatarItem(val avatarSrc: LiveData<AvatarBean>, val defaultAvatarRes: Int) : UserItem()

    class InfoItem(val iconRes: Int, val label: String, val infoSrc: LiveData<String>, val action: () -> Unit) : UserItem()
    class ActionItem(val iconRes: Int, val label: String, val action: () -> Unit) : UserItem()

}

