let ws = null;
let reconnectAttempts = 0;
const maxReconnectAttempts = 5;
const reconnectDelay = 3000; // 3秒
let heartbeatInterval = null;
const heartbeatTimeout = 30000; // 30秒
let sessionId = null; // 存储当前会话ID

// DOM 元素
const createRoomBtn = document.getElementById('createRoomBtn');
const createRoomModal = document.getElementById('createRoomModal');
const roomNameInput = document.getElementById('roomNameInput');
const confirmCreateBtn = document.getElementById('confirmCreateBtn');
const cancelCreateBtn = document.getElementById('cancelCreateBtn');

// 更新连接状态显示
function updateConnectionStatus(status, message) {
    const statusDiv = document.getElementById('connectionStatus');
    statusDiv.className = `connection-status ${status}`;
    statusDiv.textContent = message;
}

// 显示创建房间弹窗
function showCreateRoomModal() {
    createRoomModal.classList.add('show');
    roomNameInput.value = '';
    roomNameInput.focus();
}

// 隐藏创建房间弹窗
function hideCreateRoomModal() {
    createRoomModal.classList.remove('show');
}

// 创建房间
function createRoom() {
    const roomName = roomNameInput.value.trim();
    if (!roomName) {
        alert('请输入房间名称');
        return;
    }

    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            handlerType: 'createRoom',
            payload: JSON.stringify({
                roomName: roomName
            })
        }));
        hideCreateRoomModal();
    } else {
        alert('WebSocket未连接，请稍后重试');
    }
}

// 连接WebSocket
function connectWebSocket() {
    const wsUrl = 'ws://localhost:8080/chat';
    console.log('正在连接WebSocket:', wsUrl);
    
    try {
        ws = new WebSocket(wsUrl);
        
        ws.onopen = () => {
            console.log('WebSocket连接已建立');
            updateConnectionStatus('connected', '已连接到服务器');
            reconnectAttempts = 0;
            startHeartbeat();
            getSessionId();
            fetchRoomList(); // 连接成功后获取房间列表
        };
        
        ws.onclose = (event) => {
            console.log('WebSocket连接已关闭', event.code, event.reason);
            updateConnectionStatus('disconnected', '连接已断开，正在尝试重新连接...');
            stopHeartbeat();
            
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                console.log(`尝试重新连接 (${reconnectAttempts}/${maxReconnectAttempts})...`);
                setTimeout(connectWebSocket, reconnectDelay);
            } else {
                updateConnectionStatus('disconnected', '连接失败，请刷新页面重试');
            }
        };
        
        ws.onerror = (error) => {
            console.error('WebSocket错误:', error);
            updateConnectionStatus('disconnected', '连接发生错误');
        };
        
        ws.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                console.log('收到消息:', data);
                
                if (data.handlerType === 'pong') {
                    console.log('收到心跳响应');
                    return;
                }
                
                if (data.handlerType === 'createRoom') {
                    const roomData = JSON.parse(data.payload);
                    console.log('房间创建成功:', roomData);
                    fetchRoomList(); // 刷新房间列表
                }

                // 处理会话ID
                if (data.handlerType === "getSessionId") {
                    sessionId = data.payload;
                    console.log('获取到会话ID:', sessionId);
                }
            } catch (error) {
                console.error('消息解析错误:', error);
            }
        };
    } catch (error) {
        console.error('WebSocket连接错误:', error);
        updateConnectionStatus('disconnected', '连接失败，请刷新页面重试');
    }
}

// 开始心跳检测
function startHeartbeat() {
    stopHeartbeat(); // 清除可能存在的旧定时器
    heartbeatInterval = setInterval(() => {
        if (ws && ws.readyState === WebSocket.OPEN) {
            console.log('发送心跳包');
            ws.send(JSON.stringify({ handlerType: 'ping' }));
        }
    }, heartbeatTimeout);
}

// 获取会话ID
function getSessionId() {
    if (ws && ws.readyState === WebSocket.OPEN) {
        console.log('获取SessionId');
        ws.send(JSON.stringify({ handlerType: 'getSessionId' }));
    }
}

// 停止心跳检测
function stopHeartbeat() {
    if (heartbeatInterval) {
        clearInterval(heartbeatInterval);
        heartbeatInterval = null;
    }
}

// 获取房间列表
async function fetchRoomList() {
    try {
        const response = await fetch('http://localhost:8080/api/v1/rooms');
        if (!response.ok) {
            throw new Error('获取房间列表失败');
        }
        const rooms = await response.json();
        displayRooms(rooms);
    } catch (error) {
        console.error('获取房间列表错误:', error);
    }
}

// 显示房间列表
function displayRooms(rooms) {
    const roomList = document.getElementById('roomList');
    roomList.innerHTML = ''; // 清空现有列表
    
    rooms.forEach(room => {
        const isOwner = sessionId && room.ownerId === sessionId;
        const roomDiv = document.createElement('div');
        roomDiv.className = `room-item ${isOwner ? 'owner-room' : ''}`;
        
        const roomInfo = document.createElement('div');
        roomInfo.className = 'room-info';
        
        const roomName = document.createElement('span');
        roomName.className = 'room-name';
        roomName.textContent = room.roomName;
        
        if (isOwner) {
            const ownerTag = document.createElement('span');
            ownerTag.className = 'owner-tag';
            ownerTag.textContent = '房主';
            roomInfo.appendChild(ownerTag);
        }
        
        roomInfo.appendChild(roomName);
        
        const actionButton = document.createElement('button');
        actionButton.className = isOwner ? 'enter-btn' : 'join-btn';
        actionButton.textContent = isOwner ? '进入房间' : '加入房间';
        actionButton.addEventListener('click', () => {
            if (isOwner) {
                enterRoom(room.roomId);
            } else {
                joinRoom(room.roomId);
            }
        });
        
        roomDiv.appendChild(roomInfo);
        roomDiv.appendChild(actionButton);
        roomList.appendChild(roomDiv);
    });
}

// 进入房间（房主）
function enterRoom(roomId) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            handlerType: 'enterRoom',
            payload: JSON.stringify({
                roomId: roomId
            })
        }));
    }
}

// 加入房间（普通用户）
function joinRoom(roomId) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            handlerType: 'joinRoom',
            payload: JSON.stringify({
                roomId: roomId
            })
        }));
    }
}

// 事件监听器
createRoomBtn.addEventListener('click', showCreateRoomModal);
confirmCreateBtn.addEventListener('click', createRoom);
cancelCreateBtn.addEventListener('click', hideCreateRoomModal);
roomNameInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        createRoom();
    }
});

// 点击弹窗外部关闭弹窗
createRoomModal.addEventListener('click', (e) => {
    if (e.target === createRoomModal) {
        hideCreateRoomModal();
    }
});

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', () => {
    console.log('页面加载完成，开始初始化...');
    updateConnectionStatus('connecting', '正在连接服务器...');
    connectWebSocket();
}); 