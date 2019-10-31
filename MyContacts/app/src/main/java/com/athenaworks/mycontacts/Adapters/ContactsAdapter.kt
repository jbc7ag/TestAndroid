package com.athenaworks.mycontacts.Adapters

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.athenaworks.mycontacts.Models.Results
import com.athenaworks.mycontacts.R

import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class ContactsAdapter(private val context: Context, private val contacts: ArrayList<Results>) : BaseAdapter(){

    private val arraylist: ArrayList<Results>

    init {
        this.arraylist = ArrayList()
        this.arraylist.addAll(contacts)
    }

    override fun getCount(): Int {
        return contacts.size
    }

    override fun getItem(position: Int): Any {
        return contacts[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()

            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.contact_list_item, null, true)

            holder.name = convertView!!.findViewById(R.id.item_contact_name) as TextView
            holder.adress = convertView!!.findViewById(R.id.item_contact_adress) as TextView
            holder.email = convertView!!.findViewById(R.id.item_contact_email) as TextView
            holder.photo = convertView.findViewById(R.id.item_contact_img) as ImageView
            holder.star = convertView.findViewById(R.id.item_contact_fav) as CheckBox

            convertView.tag = holder

        } else {

            holder = convertView.tag as ViewHolder
        }

        //Set the data on each element on the item layout
        var items: Results = contacts[position]

        holder.name!!.setText(items.name!!.first + " "+ items.name!!.last)
        holder.adress!!.setText(items.location!!.city+", "+items.location!!.state+",  "+items.location!!.country)
        holder.email!!.setText(items.email)
        Picasso.get().load(items.picture!!.large).into(holder.photo)

        holder.star!!.setTag(position);
        holder.star!!.setChecked(isChecked(items.login!!.uuid));

        //Easy implementation on like element saved on shared preferences
        holder.star!!.setOnClickListener{

            if(holder.star!!.isChecked)
            saveOnShared(items.login!!.uuid);
        }

        return convertView
    }

    private inner class ViewHolder {

        var name: TextView? = null
        var adress: TextView? = null
        var email: TextView? = null
        var star: CheckBox? =null
        internal var photo: ImageView? = null

    }

    //help us to store if we start an user
    fun saveOnShared(Id: String?){

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonArray = JSONArray()
        jsonArray.put(Id)

        val editor = prefs.edit()
        editor.putString("users", jsonArray.toString())
        editor.commit()
    }

    //help us to know if the user is checked it should be change for a local database or an api call
    fun isChecked(Id: String?): Boolean{

        try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val jsonArray2 = JSONArray(prefs.getString("users", "[]"))

            for (i in 0 until jsonArray2.length()) {
                if( jsonArray2.getString(i) == Id)
                    return true;
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false;
        }

        return false;
    }

    // this functions help the adapter to retun the filter elements
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        contacts.clear()
        if (charText.length == 0) {
           contacts.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (
                        wp.name!!.first!!.toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.name!!.last!!.toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                   contacts.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }


    // Let the adapter add new contact to the list.
    fun addNewData(moreContacts: ArrayList<Results>) {

        contacts.clear()
        arraylist.addAll(moreContacts);
        for (wp in arraylist) {
            contacts.add(wp)
        }
        notifyDataSetChanged()
    }

}