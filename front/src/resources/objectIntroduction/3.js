export const task = `
-> class
    with name: "MyFirstClass"
-> class
    with constructor
        with name: "MyFirstClass"
        with args:
-> class
    with constructor
        with name: "MyFirstClass"
        with args: { "String", - }
`;

export const input = `public class MyFirstClass {

}`;

export const description = `<b>Konstruktor</b> jest specjalnym element każdej klasy, który jest wykonywany podczas tworzenia jej pojedynczego wystąpienia. Podstawowym zadaniem konstruktora jest zazywczaj zainicjowanie obiektu lecz mogą to być dowolne operacje, które będą służyć nadaniu obiektowi konkretnej postaci.
Podstawowymi cechami konstruktora jest to, iż nie określa on zwracanego typu oraz to, że jego nazwa jest identyczna z nazwą klasy, w której występuje (sama jego definicja może być poprzedzona opcjonalnym specyfikatorem dostępu):
<java>
NazwaKlasy(OpcjonalneParametry){
   // kod
}
</java>
Konstruktory mogą znajdować się w dowolnym miejscu w klasie, a także może ich być wiele rodzaji pod warunkiem, że będą miały inne zestawy parametrów. Należy również podkreślić, iż może istnieć klasa bez jawnie zdefiniowanego konstruktora (takie właśnie były prezentowane dotychczas). W takim wypadku zostanie użyty kontruktor domyślny (bezparametrowy) dodany automatycznie przez kompilator.`;

export const instruction = `Stwórz dwa konstruktory klasy MyFirstClass, tak aby dało się je wywołać bez żadnego parametru lub z parametrem o wartości: "TEST".`;

export const hasNext = true;