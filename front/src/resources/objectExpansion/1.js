export const task = `
-> class
    with field
        with modifiers: { "static" }
        with type: "int"
        with name: "staticField"
    with field
        with modifiers:
        with type: "int"
        with name: "field"
    with field
        with modifiers:
        with type: "MyClass"
        with name: "inner"
    with constructor
        with name: "MyClass"
        with args:
    with constructor
        with name: "MyClass"
        with args: { "int", - }
-> method
    with modifiers: { "static" }
    with result: "void"
    with name: "print"
    with args: { "MyClass", - }, { "MyClass", - }
-> method
    with modifiers: { "static" }
    with result: "MyClass"
    with name: "createInstance"
    with args:
-> method
    with modifiers:
    with result: "MyClass"
    with name: "initializeAndReturn"
    with args: { "int", - }
-> method
    with modifiers:
    with result: "void"
    with name: "print"
    with args:
`;

export const input = `public class MyClass {
    static int staticField;

    int field = -1;
    MyClass inner;

    public static void main(String[] args){
        staticField = 100;
        MyClass myClass1 = new MyClass();
        MyClass myClass2 = new MyClass(1);
        print(myClass1, myClass2);

        MyClass myClass3 = createInstance();
        myClass3.inner = new MyClass();
        myClass3.initializeAndReturn(10).print();
    }

    MyClass() {
        this.field = 0;
    }

    MyClass(int field) {
        this.field = field;
    }

    static void print(MyClass m1, MyClass m2) {
        System.out.print(m1.field);
        System.out.print(" ");
        System.out.print(m2.field);
        System.out.print(" ");
        System.out.println(new MyClass(2).field);
    }

    static MyClass createInstance() {
        return new MyClass();
    }

    MyClass initializeAndReturn(int value) {
        inner.field = 10;
        return this;
    }

    void print() {
        System.out.print("Static: ");
        System.out.print(staticField);
        System.out.print(" Field: ");
        System.out.print(field);
        System.out.print(" Inner field: ");
        System.out.println(inner.field);
    }
}`;

export const description = `Ten rozdział wykorzystuje i poszerza zdolności nabyte w szczególności z dwóch poprzednich etapów.
Dowiesz się jak w pełni korzystać z możliwości jakie daje programowanie zorientowane obiektowo. Poruszony zostanie m.in temat wykorzystywania różnych konstruktorów do wygodnego tworzenia zróżnicowanych obiektów czy też sposobu dzielenia informacji między instancjami za pośrednictwem pól statycznych.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;