package com.remote.matrix.data.firebaseDB

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.remote.domain.interfaces.IErrorCallback
import com.remote.domain.interfaces.INetsBriefRepo
import com.remote.domain.models.NetBrief

class NetsBrief (private val errorCallback: IErrorCallback): INetsBriefRepo {

    private val nets = FirebaseDatabase.getInstance(
        "https://remote-matrix-7c9ed-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(NetBrief.NETS)

    override fun getNets(callback: (ArrayList<NetBrief>) -> Unit) {
        nets.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val listOfNets = ArrayList<NetBrief>()
                for (child in it.result.children) listOfNets.add(NetInterpreter(child))
                callback(listOfNets)
            } else
                errorCallback.ErrCallback(it.exception.toString())
        }
    }

    override fun setBusy(net: NetBrief, deviceName: String) {
        nets.child(net.id).child("connectedDevices")
            .child(net.connectedDevices.size.toString()).setValue(deviceName)
    }

    override fun getNet(net: NetBrief, callback: (NetBrief) -> Unit) {
        nets.child(net.id).get().addOnCompleteListener {
            if (it.isSuccessful)
                callback(NetInterpreter(it.result))
            else
                errorCallback.ErrCallback(it.exception.toString())
        }
    }

//    fun setNets(){
//        nets.setValue(arrayListOf(
//            NetBrief("1", 3, ArrayList()),
//            NetBrief("2", 5, arrayListOf("null", "gsd")),
//            NetBrief("3", 8, arrayListOf("null"))
//        ))
//    }

    private fun snapToArrayList(snapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        for (child in snapshot.children)
            list.add(child.getValue(String::class.java)!!)
        return list
    }

    private fun NetInterpreter(snapshot: DataSnapshot): NetBrief =
        NetBrief(
            id = snapshot.key!!,//child("id").getValue(String::class.java)!!,
            countOfBMNs = snapshot.child("countOfBMNs")
                .getValue(Int::class.java)!!,
            connectedDevices = snapToArrayList(snapshot.child("connectedDevices"))
        )
}
