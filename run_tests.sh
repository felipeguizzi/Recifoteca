#!/bin/bash
# run_tests.sh

# Compila o código fonte e os testes
# Garante que o diretório 'out' existe
mkdir -p out

# Lista todos os arquivos Java, incluindo os testes
find src/main/java tests -name "*.java" > sources.txt
javac -d out/ @sources.txt

# Verifica se a compilação foi bem-sucedida
if [ $? -ne 0 ]; then
    echo "Erro de compilação."
    rm sources.txt
    exit 1
fi

# Baixa o JUnit se ainda não estiver disponível
JUNIT_PLATFORM_CONSOLE_STANDALONE="lib/junit-platform-console-standalone.jar"
if [ ! -f "$JUNIT_PLATFORM_CONSOLE_STANDALONE" ]; then
    echo "Baixando JUnit Platform Console Standalone..."
    mkdir -p lib
    curl -L -o "$JUNIT_PLATFORM_CONSOLE_STANDALONE" https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar
    if [ $? -ne 0 ]; then
        echo "Erro ao baixar o JUnit."
        rm sources.txt
        exit 1
    fi
fi

# Executa os testes usando o JUnit Platform Console
java -jar "$JUNIT_PLATFORM_CONSOLE_STANDALONE" --class-path out/ --scan-classpath

# Limpa arquivos temporários
rm sources.txt
