export const task = `
-> statement
    with resolved: "char c1 = 'X'"
    error message: "Brak definicji zmiennej c1 typu char"
-> statement
    with resolved: "Character c2 = 'X'"
    error message: "Brak definicji zmiennej c2 typu Character"
-> statement
    with text: "System.out.println(s)"
    with resolved: "System.out.println(\\"mój \\\\"ŁAŃCUCH\\\\" znaków\\")"
    error message: "Brak wywołania metody z wykorzystaniem zmiennej s"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

    }
}`;

export const description = `We wcześniejszych zadaniach konieczne było wypisywanie łańcuchów znaków. Do tego celu można użyć klasy String. Zmienne typu String umożliwiają przechowywanie ciągów znaków zawartych wewnątrz " ". Jeżeli w danym łańcuchu ma znajdować się sam znak ", konieczne jest jego poprzedzenie ukośnikiem \\.
Do przechowywania pojedynczego znaku można użyć również zmiennej typu char. Jest to jeden z typów prostych czyli elementów, które nie są instancjami konkretnych klas a co za tym idzie nie mają żadnych metod. Należy jednak podkreślić, iż każdy z typów prostych ma odpowiadający mu obiekt posiadający dedykowane mu dodatkowe funkcje. W przypadku typu znakowego jest to klasa Character. W celu przechowania pojedynczego znaku w zmiennej, konieczne jest umieszczenie go wewnątrz ' '.`;

export const instruction = `Przy wykorzystaniu poznanej wcześniej funkcji println oraz zmiennej s, wypisz na ekran ciąg znaków: mój "ŁAŃCUCH" znaków.
Dodatkowo zdefiniuj dwie zmienne c1 i c2, które są kolejno typu prostego i złożonego i przechowują znak X.`;

export const hasNext = true;