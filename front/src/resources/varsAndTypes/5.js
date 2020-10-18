export const task = `
-> statement
    with resolved: "boolean b = true"
    error message: "Brak definicji zmiennej b typu boolean"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        
    }
}`;

export const description = `Do przechowywania wartości logicznej w zmiennej używa sie typu prostego boolean lub złożonego Boolean. Może on przyjmować jedną z dwóch wartości true lub false. Zmienne logiczne są sczególnie użyteczne w przypadku instrukcji sterujących o czym dowiesz się w jednym z następnych rozdziałów.`;

export const instruction = `Zdefiniuj zmienną b odpowiedniego typu prostego, której wartością będzie odpowiedź na pytanie: "Czy jest to kurs języka Java?".`;

export const hasNext = true;