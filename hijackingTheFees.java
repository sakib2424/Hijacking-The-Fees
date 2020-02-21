import java.io.*;
import java.util.ArrayList; 

class Node {
   String guide;
   int value;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {
// this class is used to hold return values for the recursive doInsert
// routine (see below)

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}

public class hijackingTheFees {

   public static void main(String[] args) throws IOException {
	   
//     First create a new tree to store all the data
       TwoThreeTree tree = new TwoThreeTree();
//     Reading from a file
       File input = new File("/Users/Sakib/Desktop/Algo2/test4.in");
       BufferedReader reader = new BufferedReader(new FileReader(input));
       BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"));
//       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String line = reader.readLine();
//  	The first line is the database size 
       int querySize = Integer.parseInt(line);
//     Iterate through the file and add every line to the tree
       for (int i = 0; i < querySize; i++) {
    	   line = reader.readLine();
           String[] array = line.split(" ");
           int type = Integer.parseInt(array[0]);
           if (type == 1) {
        	   insert(array[1], Integer.parseInt(array[2]), tree);
           }
           else if (type == 2) {
//        	   Arrange the inputs so first one is less 
//        	   System.out.println("Adding: " + array[3] + " to " + array[1] + array[2]);
        	   String lower = "";
        	   String higher = "";
        	   if (array[1].compareTo(array[2]) > 0) {
        		   higher = array[1];
        		   lower = array[2];
        	   }
        	   else {
        		   higher = array[2];
        		   lower = array[1];
        	   }
        	   addRange(tree.root, lower, higher, 
        			   Integer.parseInt(array[3]), tree.height);
           }
           else {
        	   search(tree.root, array[1], tree.height, 0, output);
           }
       }
       
       output.flush(); 
              
   }
   
   static void search(Node node, String value, int height, int sum, BufferedWriter output) throws IOException {
	   if (height == 0 ) {
		   if (node.guide.compareTo(value) == 0) {
			   output.write(Integer.toString(node.value + sum) + "\n");
		   }
		   else {
			   output.write("-1\n");
		   }
	   }
	   else {
		   InternalNode iNode = (InternalNode) node;		   
		   if (iNode.child0.guide.compareTo(value) >= 0) {
			   sum += node.value;
			   search(iNode.child0, value, height-1, sum, output);
		   }
		   else if (iNode.child2 == null || iNode.child1.guide.compareTo(value) >= 0) {
			   sum += node.value;
			   search(iNode.child1, value, height-1, sum, output);
		   }
		   else {
			   sum += node.value;
			   search(iNode.child2, value, height-1, sum, output);
		   }
	   }
   }
   
   static void addGe(Node node, String value, int k, int height) {
	   if (height == 0) {
           if (node.guide.compareTo(value) >= 0) {
               LeafNode LNode = (LeafNode) node;
               LNode.value += k;
               
//               System.out.println("Node: " + LNode.guide + " incremented by " + 
//                       Integer.toString(k));
           }
       }
	   else {
           InternalNode iNode = (InternalNode) node;
           if (iNode.child0.guide.compareTo(value) >= 0) {
               addGe(iNode.child0, value, k, height - 1);
               incrementAll(iNode.child1, k, height - 1);
               if (iNode.child2 != null) {
            	   incrementAll(iNode.child2, k, height-1);
               }
           }
           else if (iNode.child2 == null || iNode.child1.guide.compareTo(value) >= 0) {
               addGe(iNode.child1, value, k, height - 1);
               if (iNode.child2 != null) {
            	   incrementAll(iNode.child2, k, height-1);
               }
           }
//         The first two children can be ignored
           else {
               if (iNode.child2 == null || iNode.child2.guide.compareTo(value) < 0) {
                   return;
               }
               else {
                   addGe(iNode.child2, value, k, height - 1);
               }
           }
       }
   }
   
   static void incrementAll (Node node, int k, int height) {
	   node.value += k;
   }
   
   static void addLe(Node node, String value, int k, int height) {
	   if (height == 0) {
           if (node.guide.compareTo(value) <= 0) {
               LeafNode LNode = (LeafNode) node;
               LNode.value += k;
               
//               System.out.println("Node: " + LNode.guide + " incremented by " + 
//                       Integer.toString(k));
           }
       }
	   
	   else {
		   InternalNode iNode = (InternalNode) node;
           if (iNode.child0.guide.compareTo(value) >= 0) {
               addLe(iNode.child0, value, k, height - 1);
           }
           else if (iNode.child2 == null || iNode.child1.guide.compareTo(value) >= 0) {
        	   incrementAll(iNode.child0, k, height-1);
               addLe(iNode.child1, value, k, height-1);
           }
           else {
        	   incrementAll(iNode.child0, k, height-1);
        	   incrementAll(iNode.child1, k, height-1);
               addLe(iNode.child2, value, k, height-1);
           }
	   }
   }
   
   static void addRange(Node node, String a, String b, int k, int height) {
	   if (height == 0) {
		   LeafNode LNode = (LeafNode) node;
		   if (LNode.guide.compareTo(a) >= 0 && LNode.guide.compareTo(b) <= 0) {
			   LNode.value += k;
		   }
	   }
	   else {
		   InternalNode iNode = (InternalNode) node;
		   if (iNode.child0.guide.compareTo(b) >= 0) {
               addRange(iNode.child0, a, b, k, height - 1);
           }
		   else if (iNode.child2 == null || iNode.child1.guide.compareTo(b) >= 0) {
			   if (iNode.child0.guide.compareTo(a) >= 0) {
                   addGe(iNode.child0, a, k, height - 1);
                   addLe(iNode.child1, b, k, height - 1);
               }
               else {
                   addRange(iNode.child1, a, b, k, height - 1);
               }
		   }
		   else {
               if (iNode.child0.guide.compareTo(a) >= 0) {
                   addGe(iNode.child0, a, k, height - 1);
                   incrementAll(iNode.child1, k, height - 1);
                   addLe(iNode.child2, b, k, height-1);
               }
               else if (iNode.child1.guide.compareTo(a) >= 0) {
                   addGe(iNode.child1, a, k, height - 1);
                   addLe(iNode.child2, b, k, height - 1);
               }
               else {
                   addRange(iNode.child2, a, b, k, height - 1);
               }
           }
	   }
   }
   


   static void insert(String key, int value, TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

      if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key, value, tree.root, h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key, int value, Node p, int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level, so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;
         
         // Internal Node Insert Change for lazysearch
         // Shoup's "hack" for insert
         // The “effective” value associated with a key is the sum all value fields on path from root to its leaf.
         // Thus, we can add current internal node's value to all value fields of 2 or 3 children 
         // Then, reset current internal node's value to 0
         q.child0.value += q.value;
         q.child1.value += q.value;
         if (q.child2 != null) q.child2.value += q.value;
         q.value = 0;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key, value, q.child0, h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key, value, q.child1, h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key, value, q.child2, h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q, ws.scratch);
               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q, ws.scratch, 0, 2);
                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }

   static int copyOutChildren(InternalNode q, Node[] x) {
   // copy children of q into x, and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x, Node p, int sz, int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide, and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }


   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
   // also resets guide, and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
}

hijackingTheFees
