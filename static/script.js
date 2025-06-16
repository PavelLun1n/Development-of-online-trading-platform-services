document.addEventListener('DOMContentLoaded', () => {
    const btn = document.querySelector('.dropdown-button');
    const menu = document.querySelector('.dropdown-content');
    const dropdown = document.querySelector('.dropdown');

    // Переключение меню при клике на кнопку
    btn.addEventListener('click', (e) => {
        e.stopPropagation();
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    });

    // Закрытие меню при клике вне области
    document.addEventListener('click', (e) => {
        if (!dropdown.contains(e.target)) {
            menu.style.display = 'none';
        }
    });

    // Закрытие меню при выборе категории
    menu.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', () => {
            menu.style.display = 'none';
        });
    });
});