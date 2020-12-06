export const task = `
-> statement
    with resolved: "char c = 'X'"
    log info: "Definicja zmiennej c1 typu char"
-> statement
    with text: "System.out.println(s)"
    with resolved: "System.out.println(\\"mój \\\\"ŁAŃCUCH\\\\" znaków\\")"
    log info: "Wywołanie metody z wykorzystaniem zmiennej s"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

    }
}`;

export const description = `We wcześniejszych zadaniach konieczne było wypisywanie łańcuchów znaków. Do tego celu można użyć klasy <b>String</b>. Zmienne typu <b>String</b> umożliwiają przechowywanie ciągów znaków zawartych wewnątrz <b>" "</b>. Jeżeli w danym łańcuchu ma znajdować się sam znak ", konieczne jest jego poprzedzenie ukośnikiem \\.
Do przechowywania pojedynczego znaku można użyć również zmiennej typu <b>char</b>. Jest to jeden z typów prostych czyli elementów, które nie są instancjami konkretnych klas a co za tym idzie nie mają żadnych metod. W celu przechowania pojedynczego znaku w zmiennej, konieczne jest umieszczenie go wewnątrz <b>' '</b>.`;

export const instruction = `Przy wykorzystaniu poznanej wcześniej funkcji println oraz zmiennej s, wypisz na ekran ciąg znaków: mój "ŁAŃCUCH" znaków.
Dodatkowo zdefiniuj zmienną c, która przechowuje znak X.`;

export const hasNext = true;