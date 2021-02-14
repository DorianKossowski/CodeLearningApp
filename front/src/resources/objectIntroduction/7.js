export const task = `
-> class
    with field
        with modifiers: { "static" }
        with type: "String"
        with name: "s"
        with value: "\\"MyStatic\\""
-> statement
    with text: "System.out.println(s)"
    with resolved: "System.out.println(\\"MyStatic\\")"
    log info: "Wywołanie metody z wykorzystaniem pola s"
`;

export const input = `public class MyFirstClass {

    public static void main(String[] args){
        System.out.println(s);
    }
}`;

export const description = `W klasach poza poznanymi dotychczas polami i metodami instancji, możemy korzystać z pól i metod statycznych używając specjalnego modyfikatora <b>static</b>.
Pola statyczne różnią się od pól instancji (niestatycznych) tym, że są one współdzielone przez wszystkie obiekty tej klasy, tzn. przynależą one do całej klasy, a nie konkretnie utworzonego obiektu. Ich wartości są współdzielone przez wszystkie obiekty klasy - w przeciwieństwie do pól niestatycznych, których własne egzemplarze ma każdy obiekt klasy. Są one tworzone jako pojedyncze wartości/obiekty.
Metody statyczne, natomiast, różnią się od metod niestatycznych tym, że nie mogą korzystać z pól i metod niestatycznych. Nie operują one na konkretnych egzemplarzach klasy, lecz w kontekście całej klasy. Przykładem metody statycznej jest często używana metoda startowa main.`;

export const instruction = `Odpowiednio zdefiniuj pole, tak aby na standardowe wyjście wypisany został napis: "MyStatic".`;

export const hasNext = false;