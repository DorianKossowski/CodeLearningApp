export const task = `
-> class
    with field
        with modifiers: { "static" }
        with type: "int"
        with name: "actions"
    with field
        with modifiers:
        with type: "int"
        with name: "id"
-> method
    with name: "makeAction"
    with args:
    with result: "void"
-> statement
    with text: "MyClass myClass1 = new MyClass(1)"
    in method: "main"
    log info: "Definicja zmiennej myClass1"
-> statement
    with text: "MyClass myClass2 = new MyClass(2)"
    in method: "main"
    log info: "Definicja zmiennej myClass2"
-> statement
    with text: "System.out.println(myClass1.actions)"
    with resolved: "System.out.println(2)"
    in method: "main"
    with for iteration: "0"
    log info: "Wywołanie println z parametrem myClass1.actions w pierwszej iteracji"
-> statement
    with text: "System.out.println(myClass2.actions)"
    with resolved: "System.out.println(2)"
    in method: "main"
    with for iteration: "0"
    log info: "Wywołanie println z parametrem myClass2.actions w pierwszej iteracji"
-> statement
    with text: "System.out.println(myClass1.actions)"
    with resolved: "System.out.println(4)"
    in method: "main"
    with for iteration: "1"
    log info: "Wywołanie println z parametrem myClass1.actions w drugiej iteracji"
-> statement
    with text: "System.out.println(myClass2.actions)"
    with resolved: "System.out.println(4)"
    in method: "main"
    with for iteration: "1"
    log info: "Wywołanie println z parametrem myClass2.actions w drugiej iteracji"
`;

export const input = `public class MyClass {
    static int actions;
    int id;

    public static void main(String[] args){
        MyClass myClass1 = new MyClass(1);
        MyClass myClass2 = new MyClass(2);

        for(int i=0; i<2; i=i+1) {
            myClass1.makeAction();
            myClass2.makeAction();

            System.out.println(myClass1.actions);
            System.out.println(myClass2.actions);
        }
    }

    MyClass(int id) {
        this.id = id;
    }

    void makeAction() {
        System.out.print("Akcja obiektu o id ");
        System.out.println(id);

    }
}`;

export const description = `Pola statyczne są współdzielone przez wszystkie instancje konkretnego typu, co daje im możliwość przechowywania wspólnych informacji odnośnie całej klasy. Dzięki takiej możliwości wykorzystywane są one często jako swego rodzaju liczniki związane z jakimiś działaniami na obiektach tego samego rodzaju.
Z faktu, że są one również częścią danej instancji można się do nich odwoływać tak jak do pól instancyjnych.`;

export const instruction = `Uzupełnij metodę makeAction o brakujący element pozostawiając reszte kodu bez zmian, tak aby metody wypisujące znajdujące się wewnątrz pętli wypisały kolejno dwójki oraz czwórki.`;

export const hasNext = false;