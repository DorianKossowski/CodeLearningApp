export const task = `-> method
with modifiers: { "public", "static" }
with result: "void"
with name: "main"
with args: { "String[]", - }
-> statement
in method: "main"
with text: "System.out.print(\\"Hello World\\");"
error message: "Wywołanie metody z literału"`;

export const input = `public class Hello {
    public static void main(String[] args){
        System.out.print("Hello World");
    }
}`;