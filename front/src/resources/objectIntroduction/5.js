export const task = `
-> method
    with modifiers:
    with result: "void"
    with name: "method"
    with args:
-> method
    with modifiers:
    with result: "void"
    with name: "method"
    with args: { "String", - }
-> method
    with modifiers:
    with result: "void"
    with name: "method"
    with args: { "String[]", - }
`;

export const input = `public class MyFirstClass {

    void method() {
    }
}`;

export const description = `Jak wiadomo, klasa może zawierać wiele metod, które mogą przyjmować różne rodzaje i ilości parametrów. W poprzednim ćwieczniu wspomniane zostało, jakoby dopuszczalne były takie same nazwy dla różnych metod. Taki stan jest możliwy tylko i wyłącznie po spełnieniu odpowiedniego warunku, a sytuacja ta jest nazywana <b>przeciążaniem metod</b>.
Przeciążanie pozwala na tworzenie metod o takich samych nazwach, ale różniących się przyjmowanymi parametrami. Implementacje tych metod mogą być dowolna i mogą one korzystać z dedykowanych możliwości jakie dają typy przyjmowanych parametrów.
Warto również podekreślić, że z tegoż mechanizmu skorzystano w jednym z poprzednich ćwieczń, które wymagało stworzenia konstruktorów dla różnych opcji parametrów - to nic innego jak przeciążanie konstruktorów.`;

export const instruction = `Przeciąż metodę "method" tak aby pozwalała przyjmować ona zarówno pojedynczy ciąg znaków jak i ich tablicę.`;

export const hasNext = true;