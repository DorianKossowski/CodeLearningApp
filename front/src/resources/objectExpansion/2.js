export const task = `
-> class
    with field
        with modifiers:
        with type: "int"
        with name: "field"
    with constructor
        with name: "MyClass"
        with args:
    with constructor
        with name: "MyClass"
        with args: { "int", - }
-> statement
    with text: "MyClass myClass1 = new MyClass()"
    in method: "main"
    log info: "Definicja zmiennej myClass1"
-> statement
    with text: "MyClass myClass2 = new MyClass(1)"
    in method: "main"
    log info: "Definicja zmiennej myClass2"
`;

export const input = `public class MyClass {

    public static void main(String[] args){
        MyClass myClass1 = new MyClass();
        MyClass myClass2 = ;
    }

    MyClass(int value) {
        System.out.print("Inicjalizacja wartością: ");
        System.out.println(value);
        field = value;
    }
}`;

export const description = `Wiesz już co to są konstruktory i czym się charakteryzują. Teraz dowiesz się jak z nich korzystać.
Konstruktory woła się analogicznie jak metody, jednak samo wywołanie poprzedzone jest słowem kluczowym <b>new</b>. Wynikiem takiego wywołania jest utworzenie nowego obiektu - czyli konkretnej instancji danej klasy:
<java>
NAZWA_KLASY NAZWA_OBIEKTU = new NAZWA_KLASY(OPCJONALNE_PARAM);
</java>
Dana instancja jest naturalną reprezentacją klasy. Posiadana ona swój własny stan, przechowywany w polach instancyjnych, ale także może korzystać ze współdzielonych z innymi instancjami pól i metod statycznych. Na konkretnym obiekcie możliwe jest wywoływanie metod, w których ciele możliwe jest swobodne korzystanie z jego pól.
W programie można korzystać z dowolnej ilości instancji danej klasy, można je przekazywać jako parametry, a także zwracać jako rezultaty metod.`;

export const instruction = `Nie zmieniając istniejącego kodu, uzupełnij go o brakujące elementy. W przypadku obiektu myClass2 przekaż do kontruktora literał 1.`;

export const hasNext = true;