export const task = `
-> class
    with field
        with modifiers:
        with type: "int"
        with name: "field"
    with field
        with modifiers:
        with type: "MyClass"
        with name: "inner"
-> statement
    with text: "MyClass myClass = new MyClass()"
    in method: "main"
    log info: "Definicja zmiennej myClass"
-> statement
    with text: "System.out.println(myClass.inner.field)"
    with resolved: "System.out.println(5)"
    in method: "main"
    log info: "Wywołanie println z parametrem myClass.inner.field"
`;

export const input = `public class MyClass {
    int field;
    MyClass inner;

    public static void main(String[] args){
        MyClass myClass = new MyClass();

        System.out.println(myClass.inner.field);
    }
}`;

export const description = `Pole, tak samo jak metoda, jest jedną ze składowych klasy. Powoduje to, iż odwoływanie się do pól instancji wygląda analogicznie do wołania na niej metody:
<java>
NAZWA_OBIEKTU.NAZWA_POLA
</java>
Dzięki takiemu mechanizmowi możliwe jest ciągłe korzystanie ze stanów konkretych instancji.`;

export const instruction = `Nie zmieniając istniejącego kodu, uzupełnij go w taki sposób aby na ekranie została wypisana liczba 5.`;

export const hasNext = true;