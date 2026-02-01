#include <iostream>
#include <string>

int main( ) {
int a ;
int b ;
int escolha ;
std::cout << "=== Calculadora ===" ;
std::cout << "Digite o primeiro numero: " ;
std::cin >> a ;
std::cout << "Digite o segundo numero: " ;
std::cin >> b ;
std::cout << "Digite 1 para somar, 2 para subtrair: " ;
std::cin >> escolha ;
if ( escolha > 1 ) {
a = a - b ;
std::cout << "Diferenca: " ;
}
else {
a = a + b ;
std::cout << "Soma: " ;
}
std::cout << a ;
return 0 ;
}
