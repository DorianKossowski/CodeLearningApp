export const task = `
-> class
    with field
        with modifiers: { "static" }
        with type: "String"
        with name: "name"
        with value: "\\"MyStaticName\\""
-> class
    with field
        with type: "int"
        with name: "someValue"
-> class
    with constructor
        with name: "MyClass"
        with args: { "int", "val" }
-> method
    with result: "void"
    with name: "myMethod"
    with args:
-> method
    with result: "void"
    with name: "myMethod"
    with args: { "int", - }
`;

export const input = `public class MyClass {
    static String name = "MyStaticName";
    int someValue;

    MyClass(int val) {
        someValue = val;
    }

    public static void main(String[] args){
        String s = name;
    }

    void myMethod() {
        System.out.println("Some method1");
    }

    void myMethod(int i) {
        System.out.println("Some method2");
    }
}`;

export const description = `Dotychczas omówione zostały podstawy języka Java takie jak typy zmiennych czy różnorakie instrukcje. Jest to baza niezbędna do <b>programowania obiektowego</b>, do którego właśnie Java jest przeznaczona.
Programowanie obiektowe jest swego rodzaju schematem programowania, który polega na tworzeniu programów w taki sposób, aby jak najlepiej odzwierciedlały one otaczającą nas rzeczywistość. Celem jest definiowanie obiektów (mogących posiadać swój stan oraz możliwe zachowania) i interakcji między nimi. 
Programowanie obiektowe ma na celu ułatwienie pisania, konserwacji i wielokrotnego używania programów lub ich fragmentów.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;