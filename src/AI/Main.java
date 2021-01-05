package AI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Main {

    static int nodes_expanded = 0;
    static int nodes_generated = 0;


    public static State breadth_first_search(Problem problem){
        ArrayList<State> reached = new ArrayList<>();
        ArrayList<State> movement;
        LinkedList<State> frontier = new LinkedList<State>();
        State initial = new State(problem.initial_left_state.clone(), problem.initial_right_state.clone(), null,0);
        frontier.add(initial);
        reached.add(initial);
        while(!frontier.isEmpty()){
            nodes_expanded++;
            State state = frontier.remove();
            if(problem.goal_test(state) && state.path_cost <= problem.max_time){
                return state;
            }
            if(state.is_torch_left()){
                movement = (ArrayList<State>) problem.go_right(state);
            }else{
                movement = (ArrayList<State>) problem.go_left(state);
            }
            if(movement!=null) {
                nodes_generated += movement.size();
                for (State s : movement) {
                    if(!reached.contains(s)) {
                        frontier.add(s);
                    }
                }
            }
        }

        return null;
    }

    public static State depth_first_search(Problem problem){
        ArrayList<State> movement;
        Stack<State> frontier = new Stack<>();
        frontier.push(new State(problem.initial_left_state.clone(), problem.initial_right_state.clone(), null,0));
        while(!frontier.isEmpty()){
            nodes_expanded++;
            State state = frontier.pop();
            if(problem.goal_test(state) && state.path_cost <= problem.max_time){
                return state;
            }
            if(!state.is_torch_left()){
                movement = (ArrayList<State>) problem.go_left(state);
            }else{
                movement = (ArrayList<State>) problem.go_right(state);
            }
            if(movement!=null) {
                nodes_generated += movement.size();
                for (State s : movement) {
                    frontier.push(s);
                }
            }
        }
        return null;
    }

    public static State greedy_best_first_search(Problem problem, int f){
        ArrayList<State> movement;
        ArrayList<State> reached = new ArrayList<>();
        State initial = new State(problem.initial_left_state.clone(), problem.initial_right_state.clone(), null,0, f);
        PriorityQueue<State> frontier = new PriorityQueue<>();
        frontier.add(initial);
        while(!(frontier.isEmpty())){
            nodes_expanded++;
            State state = frontier.poll();
            if(problem.goal_test(state) && state.path_cost<=problem.max_time){
                return state;
            }
            if(!state.is_torch_left()){
                movement = (ArrayList<State>) problem.go_left(state);
            }else{
                movement = (ArrayList<State>) problem.go_right(state);
            }
            if(movement!=null) {
                nodes_generated += movement.size();
                for (State s : movement) {
                    if(!reached.contains(s) || s.path_cost<reached.get(reached.indexOf(s)).path_cost){
                        frontier.add(s);
                        reached.add(s);
                    }
                }
            }

        }
        return null;
    }


    public static State a_star_search(Problem problem, int f){
        State state = greedy_best_first_search(problem, f);
        return state;
    }

    static void output(Problem problem, State state){

        String display="";
        for(int i =0;i<problem.times.length;i++){
            char symbol = (char) (65 + i);
            display += symbol + " = "+problem.times[i]+" minute(s) ";
        }

        display +="\n\n";

        if(state != null){
            JOptionPane.showMessageDialog(null, "A SOLUTION HAS BEEN FOUND !!!"+
                    "\nTotal Crossing Time: "+ state.path_cost+ " Minutes" +
                    "\nTotal Number of Nodes Generated: "+ nodes_generated +
                    "\nTotal nodes expanded: "+nodes_expanded);

            Stack<String> stack = new Stack<>();
            while(state.parent != null){
                stack.push(state.to_string());
                state = state.parent;
            }
            stack.push(state.to_string());
            while(!stack.isEmpty()){
                display += stack.pop();
            }

            JOptionPane.showMessageDialog(null, display);

        }else{
            JOptionPane.showMessageDialog(null, "NO SOLUTION HAS BEEN FOUND!!!");
        }

    }

    public static void main(String[] args) {


        String description = "The Dangerous Crossing Problem.\n\n" +
                "    - There are n people that need to cross a bridge.\n" +
                "    - Each Person crosses the bridge in x amount of time.\n" +
                "    - At most two people can cross the bridge at a given time.\n" +
                "    - If two people cross, they move at the pace of the slower person.\n" +
                "    - There is one flashlight, which is needed to cross the bridge.\n\n" +
                "The Solution will be displayed in the following way:\n" +
                "A = 1 minute, B = 2 minutes, C = 5 minutes, D = 8 minutes\n" +
                "Time Taken = 0\n" +
                "A B C D ! ====================\n\n" +
                "Time Taken = 2\n" +
                "    C D ==================== A B !\n\n" +
                "...\n" +
                "...\n\n" +
                "Time Taken = 15\n" +
                "        ==================== A B C D !\n\n" +
                "A, B, C, D represent the people crossing the bridge.\n" +
                "The flashlight is depicted by an exclamation point (!)";

        JOptionPane.showMessageDialog(null, description);

        int n = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of people crossing the bridge: "));

        int[] time_taken = new int[n];
        for (int i = 0; i < n; i++) {
            char symbol = (char) (65 + i);
            int time = Integer.parseInt(JOptionPane.showInputDialog("Enter the time taken by Person " + symbol));
            time_taken[i] = time;
        }

        int max_time = Integer.parseInt(JOptionPane.showInputDialog("Enter the minimum time to cross: "));

        String message = "Options:\n" +
                "1. Depth First Search\n" +
                "2. Breath First Search\n" +
                "3. Greedy Best First Search\n" +
                "4. A* Search\n\n" +
                "Which search mechanism would you like to use. (Enter only the number e.g. 2)";

        int option = Integer.parseInt(JOptionPane.showInputDialog(message));


        State state;

        switch (option) {
            case 1:
                Problem problem = new Problem(n, time_taken, max_time, -1);
                state = depth_first_search(problem);
                output(problem, state);
                break;

            case 2:
                problem = new Problem(n, time_taken, max_time, -1);
                state = breadth_first_search(problem);
                output(problem, state);
                break;

            case 3:problem = new Problem(n, time_taken, max_time, 0);
                state = greedy_best_first_search(problem, 0);

                output(problem, state);
                break;
            case 4:
                problem = new Problem(n, time_taken, max_time, 1);
                state = a_star_search(problem, 1);
                output(problem, state);
                break;

            default:
                state = null;
        }
    }


}
