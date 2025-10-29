#!/bin/bash

# Parse arguments
VERBOSE=false
if [[ "$1" == "--verbose" ]]; then
    VERBOSE=true
fi

# Function to run command with optional log suppression
run_cmd() {
    if $VERBOSE; then
        "$@"
    else
        "$@" > /dev/null 2>&1
    fi
}

echo "🚀 Starting Velocity Proxy..."
cd ../velocity-server
run_cmd java -jar velocity.jar &
VELOCITY_PID=$!
echo "✅ Velocity started with PID $VELOCITY_PID"

sleep 5

echo "🏠 Starting Lobby Server..."
cd ../lobby-server
run_cmd java -jar paper.jar nogui &
LOBBY_PID=$!
echo "✅ Lobby started with PID $LOBBY_PID"

sleep 5

echo "⚔️ Starting Game Server..."
cd ../game-server
run_cmd java -jar paper.jar nogui &
GAME_PID=$!
echo "✅ Game started with PID $GAME_PID"

sleep 10

echo "🤖 Running Bots..."
cd ../scripts
run_cmd npm install
run_cmd node bot1.js &
BOT1_PID=$!
run_cmd node bot2.js &
BOT2_PID=$!
run_cmd node bot3.js &
BOT3_PID=$!
echo "✅ Bots started with PIDs $BOT1_PID $BOT2_PID $BOT3_PID"

# Wait for test to complete, say 30 seconds
echo "⏳ Waiting for test to complete..."
sleep 30

echo "🛑 Shutting down..."
kill $BOT1_PID $BOT2_PID $BOT3_PID 2>/dev/null
kill $GAME_PID 2>/dev/null
kill $LOBBY_PID 2>/dev/null
kill $VELOCITY_PID 2>/dev/null

echo "✅ All processes shut down."