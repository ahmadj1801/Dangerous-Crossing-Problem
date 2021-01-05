package AI;


public class State implements Comparable<State>{

    int[] current_left_state;
    int[] current_right_state;
    State parent;
    int path_cost;
    int depth;
    boolean torch;
    int heuristic_value; //theory cost to goal - minimum number of moves
    int heuristic_type;


    State(int[] left, int[] right, State parent, int path_cost){
        this.current_left_state = left;
        this.current_right_state = right;
        this.parent = parent;
        this.path_cost=path_cost;

        if(parent == null){
            this.depth =0;
            this.path_cost=0;
            this.torch = true;
        }else{
            this.depth = parent.depth + 1;
            this.torch = !parent.torch;
        }
    }

    State(int[] left, int[] right, State parent, int path_cost, int f){
        this.current_left_state = left;
        this.current_right_state = right;
        this.parent = parent;
        this.path_cost = path_cost;
        if(parent == null){
            this.depth =0;
            this.path_cost=0;
            this.torch = true;
        }else{
            this.depth = parent.depth + 1;
            this.torch = !parent.torch;
        }
        this.heuristic_type = f;
        this.heuristic_value = calculate_heuristic();
    }

    boolean is_torch_left(){ return torch; }

    /*The heuristic being used calculates the number of people that need to cross.
     * Thereafter, we calculate the number of moves needed to transport the people.
     * Relax the Problem:
     *   - People can cross without flashlight
     *   - Can cross in pairs of two and a sole ranger if odd num of people*/
    public int calculate_heuristic(){
        int h=0;
        for(int i=0; i<this.current_left_state.length; i++){
            if(current_left_state[i]==1){
                h++;
            }
        }
        if(h%2==0){
            h = h/2;
        }else{
            h = h/2 + 1;
        }

        if(this.heuristic_type==1){
            h = h + this.path_cost;
        }
        return h;
    }

    String to_string(){
        String output ="Time Taken = "+this.path_cost+"\n";
        for(int i=0; i<current_left_state.length; i++){
            if(current_left_state[i]==1){
                output += (char) (65 + i)+" ";
            }
        }
        if(torch){
            output += "!";
        }
        output += " ==================== ";
        for(int i=0; i<current_right_state.length; i++){
            if(current_right_state[i]==1){
                output += (char) (65 + i)+" ";
            }
        }
        if(!torch){
            output += "!";
        }
        output += "\n\n";
        return output;
    }

    @Override
    public int compareTo(State state) {
        return this.heuristic_value<state.heuristic_value?-1:1; //ascending order
    }

}
