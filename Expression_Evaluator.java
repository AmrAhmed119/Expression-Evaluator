import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.security.AlgorithmConstraints;
import java.util.regex.*;

interface IExpressionEvaluator {
  
    public String infixToPostfix(String expression);
  
    public int evaluate(String expression);

}

interface IStack {

    public Object pop();

    public Object peek();

    public void push(Object element);

    public boolean isEmpty();

    public int size();

}

class MyStack implements IStack {

    class Node {
        Object data;
        Node next;
    }

    Node top = null;
    static int size = 0;

    /*** Pushes an item onto the top of this stack.
    * @param object to insert*
    */
    public void push(Object element) {

        Node temp = new Node();
        temp.data = element;
        if (top == null) {
            temp.next = null;
            top = temp;
        } else {
            temp.next = top;
            top = temp;
        }
        size++;

    }

    /*** Removes the element at the top of stack and returnsthat element.
    * @return top of stack element, or through exception if empty
    */
    public Object pop() {

        if (top == null)
            throw new EmptyStackException();

        Object curr = top.data;
        top = top.next;
        size--;
        return curr;

    }

    /*** Tests if this stack is empty
    * @return true if stack empty
    */
    public boolean isEmpty() {

        if (top == null) {
            return true;
        }
        return false;

    }

    /*** Get the element at the top of stack without removing it from stack.
    * @return top of stack element, or through exception if empty
    */
    public Object peek() {
        if (top == null)
            throw new EmptyStackException();
        return top.data;
    }

    /**
     * @return returns the size of the stack
     */
    public int size() {
        return size;
    }

    /**
     * function used to print the stack elements with specific formula
     */
    public void show() {

        Node current = top;
        System.out.print("[");
        for (int i = 0; current != null; ++i) {
            System.out.print(current.data);
            if (current.next != null)
                System.out.print(", ");
            current = current.next;
        }
        System.out.print("]");

    }
}

public class Evaluator implements IExpressionEvaluator{

    static int a,b,c;

    /**
     * takes an operator and returns the equivalent priority of this operator
     * @param a
     * @return the priority of the operator 
     */
    int prio(char a){
        if(a=='+' || a=='-') return 1;
        else if(a=='*' || a=='/') return 2;
        else if(a=='^') return 3;
        else return 0;
    }


    /**
    * Takes a symbolic/numeric infix expression as input and converts it to
    * postfix notation. There is no assumption on spaces between terms or the
    * length of the term (e.g., two digits symbolic or numeric term)
    *
    * @param expression infix expression
    * @return postfix expression
    */
    public String infixToPostfix(String expression){

        MyStack stack = new MyStack();
        String postfix = "";
        int count1 = 0;
        int count2 = 0;
        for(int i=0;i<expression.length();i++){

            char op = expression.charAt(i);
            if(op=='a' || op=='b' || op=='c'){ 
                postfix += op;
                count1++;
            }
                
            else if( op=='+' || op=='-' || op=='*' || op=='/' || op=='^'){

                count2++;
                while( !stack.isEmpty() && prio(op) <= prio(String.valueOf(stack.peek()).charAt(0)) ){
                    postfix += String.valueOf(stack.pop());
                }
                stack.push(op);

            }
            else if(op=='(') stack.push(op);
            
            else if(op==')'){
                while( !(String.valueOf(stack.peek()).equals("("))  ){
                    postfix += String.valueOf(stack.pop());
                }
                stack.pop();
            }

            else throw new ArithmeticException();
        }
        if(count1<count2) throw new ArithmeticException();
        while(!stack.isEmpty()) postfix += String.valueOf(stack.pop());
        return postfix;

    }

    /**
     * takes the evaluator stack and char operator
     * and forms the operation on on the stack elements
     * to evaluate the postfix expression
     * @param stack
     * @param op
     */
    void solve(MyStack stack,char op){

        int oper1=0, oper2=0;   //first,second operands
        String pop2 = String.valueOf(stack.pop());  
        String pop1 = String.valueOf(stack.pop());  

        //convert each letter by it's specific number if letter exist otherwise take the number
        if(pop1.equals("a")) oper1 = a; 
        else if(pop1.equals("b")) oper1 = b; 
        else if(pop1.equals("c")) oper1 = c; 
        else oper1 = Integer.parseInt(pop1);

        if(pop2.equals("a")) oper2 = a; 
        else if(pop2.equals("b")) oper2 = b; 
        else if(pop2.equals("c")) oper2 = c; 
        else oper2 = Integer.parseInt(pop2);

        String res = new String();
    
        switch(op){

            case '+': res = String.valueOf(oper1+oper2); break;
            case '-': res = String.valueOf(oper1-oper2); break;
            case '*': res = String.valueOf(oper1*oper2); break;
            case '/': res = String.valueOf(oper1/oper2); break;
            case '^': res = String.valueOf((int)Math.pow(oper1,oper2)); break;

        }
        stack.push(res);

    }

    /**
    * Evaluate a postfix numeric expression, with a single space separator
    * @param expression postfix expression
    * @return the expression evaluated value
    */
    public int evaluate(String expression){

        MyStack stack = new MyStack();

        for(int i=0;i<expression.length();i++){

            char op = expression.charAt(i);

            if(op=='a' || op=='b' || op=='c') stack.push(op);
            
            else if(op=='+' || op=='*' || op=='/' || op=='^'){   
                solve(stack, op);
            }
            else if(op=='-'){   
                
                if(stack.size()==1){

                    String pop = String.valueOf(stack.pop());

                    int oper = 0;
                    if(pop.equals("a")) oper = a; 
                    else if(pop.equals("b")) oper = b; 
                    else if(pop.equals("c")) oper = c; 
                    else oper = Integer.parseInt(pop);
                    String diff = String.valueOf(-oper);
                    stack.push(diff);

                }
                else{
                    solve(stack, op);
                }
                
            }

        }
        if(stack.size()>=2) throw new ArithmeticException();
        else 
        return Integer.parseInt(String.valueOf(stack.pop()));

    }

    public static void main(String[] args) {

        Scanner read = new Scanner(System.in);

        try{

            String infix = read.nextLine().replaceAll("\\s", "");
            infix = infix.replaceAll("--", "+");
            String l1 = read.nextLine().replaceAll("\\s", "");
            String l2 = read.nextLine().replaceAll("\\s", "");
            String l3 = read.nextLine().replaceAll("\\s", "");
            int count1=0,count2=0;


            for(int i=0;i<infix.length()-1;i++){
                if(infix.charAt(i)=='(' && infix.charAt(i+1)==')') throw new ArithmeticException();
            }
            for(int i=0;i<infix.length();i++){
                if(infix.charAt(i)=='(') count1++;
                else if(infix.charAt(i)==')') count2++;
            }
            if(count1!=count2) throw new ArithmeticException();

            
            a = Integer.parseInt(l1.replaceAll("a=", ""));
            b = Integer.parseInt(l2.replaceAll("b=", ""));
            c = Integer.parseInt(l3.replaceAll("c=", ""));
            if(infix.charAt(0)=='+') infix = infix.substring(1);
            infix = infix.replace("/+", "/");
            infix = infix.replace("-+", "-");
            infix = infix.replace("*+", "*");
            infix = infix.replace("^+", "^");
            infix = infix.replace("++", "+");
            

            if(infix.length()==1 && (infix.charAt(0)=='a' ||infix.charAt(0)=='b' || infix.charAt(0)=='c' )) 
            {
                System.out.println(infix.charAt(0));
                int ans=0;
                if(infix.charAt(0)=='a') ans = a;
                else if(infix.charAt(0)=='b') ans = b;
                else ans = c;
                System.out.println(ans);
                return;
            }
            
            
            for(int i=0;i<infix.length()-1;i++){
                int c =0;
                if( infix.charAt(i)=='/' && infix.charAt(i+1)=='/' || infix.charAt(i)=='*' && infix.charAt(i+1)=='*' || infix.charAt(i)=='^' && infix.charAt(i+1)=='^' ) throw new ArithmeticException();
            }
            if(infix.charAt(0)=='*' || infix.charAt(0)=='/' || infix.charAt(0)=='^' ) throw new ArithmeticException();
            if(infix.charAt(infix.length()-1)=='*' || infix.charAt(infix.length()-1)=='/' || infix.charAt(infix.length()-1)=='^'  || infix.charAt(infix.length()-1)=='-' || infix.charAt(infix.length()-1)=='+'  ) throw new ArithmeticException();


            Evaluator obj = new Evaluator();
            String postfix = obj.infixToPostfix(infix);
            System.out.println(postfix);
            int ans = obj.evaluate(postfix);
            System.out.println(ans);
        
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }
}


