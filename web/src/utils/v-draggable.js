import configInfoStore from "../stores/config";

function setupDraggable(el, enabled = true) {
    // 清除旧的拖拽监听器（避免重复绑定）
    if (el._cleanupDrag) {
        el._cleanupDrag();
    }
    
    // 如果禁用拖拽，则直接返回
    if (!enabled) {
        return;
    }

    let startX = 0;
    let startY = 0;
    let currentX = 0;
    let currentY = 0;
    
    // 获取配置存储实例
    const configStore = configInfoStore();

    // 获取元素初始位置
    const rect = el.getBoundingClientRect();
    const computedStyle = window.getComputedStyle(el);
    
    // 如果元素已经有明确的位置设置，则使用该位置
    if (computedStyle.position === 'fixed') {
        // 检查是否有保存的位置信息
        const savedX = configStore.float_left;
        const savedY = window.innerHeight - configStore.float_bottom - rect.height;
        
        if (savedX !== undefined && savedY !== undefined) {
            // 使用保存的位置
            currentX = savedX;
            currentY = savedY;
        } else {
            // 如果已经设置了 right 和 bottom 属性，则根据这些值计算初始位置
            const right = computedStyle.right;
            const bottom = computedStyle.bottom;
            
            if (right !== 'auto' && !isNaN(parseFloat(right))) {
                currentX = window.innerWidth - rect.width - parseFloat(right);
            } else if (rect.left !== undefined) {
                currentX = rect.left;
            }
            
            if (bottom !== 'auto' && !isNaN(parseFloat(bottom))) {
                currentY = window.innerHeight - rect.height - parseFloat(bottom);
            } else if (rect.top !== undefined) {
                currentY = rect.top;
            }
        }
    } else {
        // 默认位置
        currentX = rect.left;
        currentY = rect.top;
    }

    // 初始化位置信息
    el.dataset.currentX = String(currentX);
    el.dataset.currentY = String(currentY);

    // 设置样式
    el.style.position = "fixed";
    el.style.cursor = "move";
    // 移除可能干扰的定位属性
    el.style.removeProperty('top');
    el.style.removeProperty('left');
    el.style.removeProperty('right');
    el.style.removeProperty('bottom');
    el.style.transform = `translate(${currentX}px, ${currentY}px)`;

    // 鼠标按下时，准备拖动
    const onMouseDown = (event) => {
        event.preventDefault();

        startX = event.clientX - Number(el.dataset.currentX);
        startY = event.clientY - Number(el.dataset.currentY);

        document.addEventListener("mousemove", onMouseMove);
        document.addEventListener("mouseup", onMouseUp);
    };

    // 拖动过程中
    const onMouseMove = (event) => {
        const deltaX = event.clientX - startX;
        const deltaY = event.clientY - startY;

        const { width, height } = el.getBoundingClientRect();
        const maxX = window.innerWidth - width;
        const maxY = window.innerHeight - height;

        const newX = Math.max(0, Math.min(deltaX, maxX));
        const newY = Math.max(0, Math.min(deltaY, maxY));

        el.dataset.currentX = String(newX);
        el.dataset.currentY = String(newY);
        el.style.transform = `translate(${newX}px, ${newY}px)`;
    };

    // 拖动结束，移除监听器并保存位置
    const onMouseUp = () => {
        //查看当前距离 bottom 和 right  距离
        const currentX = Number(el.dataset.currentX);
        const currentY = Number(el.dataset.currentY);
        const { width, height } = el.getBoundingClientRect();
        
        // 当前距离右边和底边的距离
        const distanceToRight = window.innerWidth - (currentX + width);
        const distanceToBottom = window.innerHeight - (currentY + height);
        
        // 保存位置信息到配置存储
        configStore.float_left = currentX;
        configStore.float_bottom = distanceToBottom;
        
        console.log(`距离右边: ${distanceToRight}px, 距离底边: ${distanceToBottom}px`);

        document.removeEventListener("mousemove", onMouseMove);
        document.removeEventListener("mouseup", onMouseUp);
    };

    // 窗口大小改变时的处理函数
    const onResize = () => {
        const { width, height } = el.getBoundingClientRect();
        let currentX = Number(el.dataset.currentX);
        let currentY = Number(el.dataset.currentY);
        
        // 确保元素在窗口边界内
        const maxX = window.innerWidth - width;
        const maxY = window.innerHeight - height;
        
        currentX = Math.max(0, Math.min(currentX, maxX));
        currentY = Math.max(0, Math.min(currentY, maxY));
        
        // 更新位置信息
        el.dataset.currentX = String(currentX);
        el.dataset.currentY = String(currentY);
        el.style.transform = `translate(${currentX}px, ${currentY}px)`;
    };

    // 绑定初始事件
    el.addEventListener("mousedown", onMouseDown);
    window.addEventListener("resize", onResize);

    // 提供清理函数
    el._cleanupDrag = () => {
        el.removeEventListener("mousedown", onMouseDown);
        document.removeEventListener("mousemove", onMouseMove);
        document.removeEventListener("mouseup", onMouseUp);
        window.removeEventListener("resize", onResize);
    };
}

// Vue 指令定义
const vDraggable = {
    mounted(el, binding) {
        setupDraggable(el, binding.value !== false);
    },
    updated(el, binding) {
        // 元素更新时重新设置拖拽功能
        setupDraggable(el, binding.value !== false);
    },
    unmounted(el) {
        if (el._cleanupDrag) {
            el._cleanupDrag();
        }
    },
};

export default vDraggable;