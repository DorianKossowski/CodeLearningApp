export const task = `
-> class
    with field
        with modifiers: { "static" }
        with name: "field"
-> method
    with modifiers: { "static" }
    with result: "void"
    with name: "fun"
    with args:
-> method
    with modifiers: { "static" }
    with result: "void"
    with name: "funWithParam"
    with args: { "int", "value" }
-> statement
    with resolved: "fun()"
    in method: "main"
    log info: "Wywołanie metody fun w main"
-> statement
    with resolved: "funWithParam(5)"
    in method: "main"
    log info: "Wywołanie metody funWithParam z odpowiednim parametrem w main"
-> statement
    with resolved: "System.out.println(\\"My no param method\\")"
    in method: "fun"
    log info: "Wywołanie metody println w fun"
-> statement
    with resolved: "System.out.print(\\"My param: \\")"
    in method: "funWithParam"
    log info: "Wywołanie metody print w funWithParam"
-> statement
    with resolved: "System.out.println(5)"
    in method: "funWithParam"
    log info: "Wywołanie metody println w funWithParam"
-> statement
    with resolved: "System.out.print(\\"My field: \\")"
    in method: "funWithParam"
    log info: "Wywołanie metody print w funWithParam"
-> statement
    with resolved: "System.out.println(10)"
    in method: "funWithParam"
    log info: "Wywołanie metody println w funWithParam"
`;

export const input = `public class MyClass {
    
    public static void main(String[] args){
    }

    static void fun() {
        System.out.println("My no param method");
    }

    static void funWithParam(int value) {
        System.out.print("My param: ");
        System.out.println(value);
        System.out.print("My field: ");
        System.out.println(field);
    }
}`;

export const description = `Wywołanie metody statycznej sprowadza się do instrukcji:
<java>
NAZWA_METODY(OPCJONALNE_PARAM);
</java>
W tym momencie wykonywany zostaje kod logiki zaimplementowanej w danej metodzie. Jak wiadomo metody mogą przyjmować parametry i to właśnie one są kluczowe przy wybieraniu odpowiedniej implementacji w przypadku wielu przeciążeń danej metody. W celu przekazania określonych parametrów umieszcza się je w przedstawionym powyżej nawiasie okrągłym, z naciskiem na typy które muszą się pokrywać z tymi, podanymi w definicji metody.
Jak wiadomo paremetry są opcjonalne, jednak możliwe jest również korzystanie z pól statycznych wewnątrz logiki metody.`;

export const instruction = `Nie ingerując w metody fun oraz funWithParam, umieść w metodzie main odpowiednie wywołania oraz uzupełnij brakujące elementy klasy, tak aby na ekran zostały wypisane w kolejnych liniach: "My no param method", "My param: 5" oraz "My field: 10".`;

export const hasNext = true;