#!/bin/zsh

echo "🔍 Verificando porta 8080..."
PID=$(lsof -ti:8080)

if [ -n "$PID" ]; then
  echo "⚠️  Processo $PID encontrado na porta 8080. Encerrando..."
  kill -9 $PID
  sleep 1
  echo "✅ Processo encerrado."
else
  echo "✅ Porta 8080 livre."
fi

echo "🚀 Iniciando aplicação..."
./mvnw spring-boot:run

