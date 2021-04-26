export const task = `
-> class
    with field
        with modifiers:
        with type: "int"
        with name: "field"
-> method
    with name: "setField"
    with args: { -, "field" }
    with result: "void"
-> statement
    with text: "MyClass myClass = new MyClass()"
    in method: "main"
    log info: "Definicja zmiennej myClass"
-> statement
    with text: "System.out.println(myClass.field)"
    with resolved: "System.out.println(1)"
    in method: "main"
    log info: "Wywołanie println z parametrem myClass.field"
`;

export const input = `public class MyClass {
    int field;

    public static void main(String[] args){
        MyClass myClass = new MyClass();

        myClass.printAndReturn().setField(1);

        System.out.println(myClass.field);
    }

    MyClass printAndReturn() {
        System.out.println(this.field);
        return ;
    }


}`;

export const description = `Słowo kluczowe <b>this</b> pozwala odwoływać się do aktualnego obiektu. Jest ono dostępne w każdym niestatycznym kontekście i odpowiada obiektowi na rzecz którego wywoływana jest dana metoda.
Pozwala ono na jawne odwoływanie się do pól danej instancji (możliwe jest również jego pomijanie z czym miałeś doczynienia dotychczas) dzięki czemu możliwe jest odróżnienie pola danego obiektu od parametru o takiej samej nazwie.
This odwołuje się do całej instancji co pozwala na zwracanie z metody samej siebie i budowanie ciągu wywołań metod na jednym obiekcie.`;

export const instruction = `Nie zmieniając istniejącego kodu, uzupełnij go o brakujące elementy w taki sposób aby na ekranie została wypisana liczba 0 oraz 1. Parametr metody setField powinien mieć nazwę field.`;

export const hasNext = true;