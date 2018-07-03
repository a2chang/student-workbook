package com.aerospike;

import java.util.ArrayList;
import java.util.List;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.ListOperation;
import com.aerospike.client.cdt.ListPolicy;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.ListWriteFlags;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

public class AddToList {
	public static void insertOrdered(AerospikeClient client, int value) {
		Key key = new Key("test", "s1", 1);  //Record 1
                ListPolicy lPolicy = new ListPolicy(ListOrder.ORDERED, ListWriteFlags.DEFAULT); 
                if(value == 10){  //First item, idenitfy List Order. Extra Credit A) Try changing 10 to 11!
		client.operate(null, key, 
                          ListOperation.append(lPolicy, "myList", Value.get(value))
                        );
                } else {
		client.operate(null, key, 
                          ListOperation.append("myList", Value.get(value))
                         );
                }
	}
	public static void insertUnordered(AerospikeClient client, int value) {
		Key key = new Key("test", "s1", 2);  //Record 2
                ListPolicy lPolicy = new ListPolicy(ListOrder.UNORDERED, ListWriteFlags.DEFAULT); 
                if(value == 10){  //First item, idenitfy List Order. UNORDERED is also DEFAULT 
		client.operate(null, key, 
                          ListOperation.append(lPolicy, "myList", Value.get(value))
                        );
                } else {
		client.operate(null, key, 
                          ListOperation.append("myList", Value.get(value))
                         );
                }
	}
	public static void insertUnordered_setOrder(AerospikeClient client, int value) {
		Key key = new Key("test", "s1", 3);  //Record 3 
                ListPolicy lPolicy = new ListPolicy(ListOrder.UNORDERED, ListWriteFlags.DEFAULT); 
                if(value != 0){  //First loop items, idenitfy List Order. UNORDERED is also DEFAULT 
		client.operate(null, key, 
                          ListOperation.append(lPolicy, "myList", Value.get(value))
                        );
                } else {
		client.operate(null, key,   //Extra Credit B) Comment out setOrder and retry. 
                          ListOperation.setOrder("myList", ListOrder.ORDERED), 
                          ListOperation.append("myList", Value.get(value))
                         );
                }
	}


	public static void main(String[] args) {
		AerospikeClient client = new AerospikeClient("127.0.0.1", 3000);
		

		Key key1 = new Key("test", "s1", 1);
		Key key2 = new Key("test", "s1", 2);
		Key key3 = new Key("test", "s1", 3);
		WritePolicy policy = new WritePolicy();
		policy.recordExistsAction = RecordExistsAction.UPDATE;
		
		client.delete(policy, key1);
		client.delete(policy, key2);
		client.delete(policy, key3);

                Bin bin1 = new Bin("id","key1");
                Bin bin2 = new Bin("id","key2");
                Bin bin3 = new Bin("id","key3");

                client.put(policy, key1, bin1);
                client.put(policy, key2, bin2);
                client.put(policy, key3, bin3);


		for (int i = 10; i < 20; i++) {
			insertOrdered(client,  i);
			insertUnordered(client,  i);
			insertUnordered_setOrder(client,  i);
		}
		for (int i = 0; i < 10; i++) {
			insertOrdered(client,  i);
			insertUnordered(client,  i);
			insertUnordered_setOrder(client,  i);
		}
		client.close();
	}
}
