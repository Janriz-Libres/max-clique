/* 
 * A node in the sorted list based on the degrees of the graph nodes
 * in the induced subgraph of possible additions (PA)
 *
 * @author Shalin Shah
 * Email: shah.shalin@gmail.com
 */
public class SortedListNode implements Comparable
{
    public int node;
    public int reach;

    public SortedListNode(int node, int reach) {
        this.node = node;
        this.reach = reach;
    }
    
    @Override
    public int compareTo(Object obj) {
        SortedListNode n = (SortedListNode)obj;

        if(n.reach > this.reach)
            return 1;
        
        if(n.reach < this.reach) 
            return -1;
        
        return 0;
    }
}

