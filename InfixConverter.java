import java.util.*;

public class InfixConverter {

    // Check operator precedence
    static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        if (op.equals("^")) return 3;
        return -1;
    }

    // Check if token is operator
    static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
               token.equals("*") || token.equals("/") ||
               token.equals("^");
    }

    // Convert infix to postfix
    static String infixToPostfix(List<String> tokens) {
        Stack<String> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {

            // If number/operand → add to result
            if (token.matches("\\d+") || token.matches("[A-Za-z]+")) {
                result.append(token).append(" ");
            }

            // If '('
            else if (token.equals("(")) {
                stack.push(token);
            }

            // If ')'
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                }
                if (stack.isEmpty()) throw new RuntimeException("Mismatched parentheses!");
                stack.pop(); // remove '('
            }

            // If operator
            else if (isOperator(token)) {
                while (!stack.isEmpty() &&
                        precedence(token) <= precedence(stack.peek())) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }

            else {
                throw new RuntimeException("Invalid character: " + token);
            }
        }

        // Pop remaining operators
        while (!stack.isEmpty()) {
            if (stack.peek().equals("("))
                throw new RuntimeException("Mismatched parentheses!");
            result.append(stack.pop()).append(" ");
        }

        return result.toString().trim();
    }

    // Convert infix to prefix
    static String infixToPrefix(List<String> tokens) {
        // Reverse tokens
        Collections.reverse(tokens);

        // Swap brackets
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("("))
                tokens.set(i, ")");
            else if (tokens.get(i).equals(")"))
                tokens.set(i, "(");
        }

        // Get postfix of reversed expression
        String postfix = infixToPostfix(tokens);

        // Reverse postfix to get prefix
        List<String> prefixTokens = Arrays.asList(postfix.split(" "));
        Collections.reverse(prefixTokens);

        return String.join(" ", prefixTokens);
    }

    // Tokenize input (supports multi-digit numbers)
    static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();

        for (char ch : input.toCharArray()) {

            if (Character.isDigit(ch)) {
                number.append(ch); // build multi-digit number
            } 
            else {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }

                if (ch == ' ') continue;

                if ("+-*/^()".indexOf(ch) != -1) {
                    tokens.add(String.valueOf(ch));
                } else {
                    throw new RuntimeException("Invalid character: " + ch);
                }
            }
        }

        if (number.length() > 0) {
            tokens.add(number.toString());
        }

        return tokens;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Infix Expression: ");
            String input = scanner.nextLine();

            // Convert to tokens
            List<String> tokens = tokenize(input);

            // Process
            String postfix = infixToPostfix(tokens);
            String prefix = infixToPrefix(new ArrayList<>(tokens));

            // Output
            System.out.println("Infix:   " + input);
            System.out.println("Postfix: " + postfix);
            System.out.println("Prefix:  " + prefix);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}