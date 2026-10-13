# Views & Resources

**Цели работы**: 
1. Научиться верстать экраны в XML используя библиотеку компонентов, 
2. Научиться правильно организовывать ресурсы/цвета для легкого переключения темы
3. Научиться пользоватся Figma для верстки

**Предварительное условие**:
1. Аккаунт в [Figma](https://www.figma.com/)
2. Откройте макеты в Figma по [ссылке](https://www.figma.com/community/file/1433109187156322315/views-and-resources) в своем аккаунте

## Задание 1

В этом задании вам необходимо собрать layout по макетам в Figma

1. Используйте гайдлайны и имплементацию классов из библиотеки [Material Components](https://m3.material.io/components)
2. Иконки можно взять из готовых material ассетов в студии или с сайта [https://materialdesignicons.com](https://materialdesignicons.com/)
3. Используйте директорию `res` для строк, картинок и прочих ресурсов
4. Картинки для корзины - по аашему выбору

Задание содержит 3 страницы:
1. `Contacts` - страница контакта записной книжки
2. `Cart` - страница корзины
3. `Login` - [MaterialAlertDialog](https://m2.material.io/components/dialogs/android) формы логина

Для первого и второго задания в проекте сделаны две пустых `Activity`:
1. `ContactsActivity`
2. `CartActivity`

Для третьего задания, в `MainActivity` добавлено отображение `MaterialAlertDialog` при нажатии на кнопку `Show Sign In Dialog`.

### Дополнительно

1. Не забывайте про ландшафтный режим. Верстка должна оставаться рабочей и в этом режиме.
2. Будет плюсом, если список пунктов в корзине будет реализован через `RecyclerView` или собственный `ViewGroup` с переиспользованием элементов.

## Задание 2

На каждой странице макетов в Figma есть темная тема. 
Воспользуйтесь `res/values` и `res/values-night` для реализации светлой и темной темы.
Обязательно используйте аттрибуты темы в верстке для реализации этой функциональности.

## Полезные ссылки

Настоятельно рекомендуется ознакомиться с следующими ресурсами:

1. [Material Design](https://material.io/design) - официальный сайт Material Design
2. [Material Components](https://m3.material.io/components) - библиотека компонентов Material Design
3. [Material Colors](https://m2.material.io/design/color/the-color-system.html#color-usage-and-palettes) - цветовая палитра Material Design и их использование
4. [Material theme builder](https://m3.material.io/blog/material-theme-builder) - генератор тем Material Design

## Реализация и проверка

- Contacts, Cart и Login сверены со светлыми и тёмными вариантами предоставленного
  [макета Figma](https://www.figma.com/design/Hnr56IYCNZ7kREAB63VuhQ/Views-and-resources--Community-).
- Цвета находятся в `values/colors.xml` и `values-night/colors.xml`; XML использует
  атрибуты общей темы. Для горизонтальной корзины предусмотрена двухколоночная
  верстка, формы прокручиваются при нехватке места.
- Login использует `MaterialAlertDialog` внутри `DialogFragment`, поэтому введённые
  значения сохраняются при пересоздании Activity. Это демонстрационная форма:
  авторизация, сброс пароля, сохранение контакта и оформление заказа не входят в ТЗ.
  Демонстрационные пароли не передаются менеджеру автозаполнения.
- Иконки — оригинальные Android VectorDrawable из
  [Google Material Design Icons](https://github.com/google/material-design-icons/tree/49d4db35df873165d6bd6ba09b063c7dafbac2f4/android).
  Использованы Material Icons, для `light_mode` — Outlined. Лицензия Apache 2.0
  сохранена в `third-party/material-icons-LICENSE.txt`.

Проверка сборки и ресурсов:

```sh
./gradlew :app:assembleDebug :app:lintDebug :app:assembleDebugAndroidTest
```

Семь инструментальных тестов в `LayoutTest` проверяют три экрана в обеих темах
и ориентациях, выравнивание цен, выбор типа телефона, сохранение ввода после
пересоздания Activity, отсутствие дублирующихся диалогов и доступность кнопок
Login с открытой клавиатурой в landscape. Видимость IME проверяется явно.
Скриншоты сохраняются в каталоге `files/screenshots` внешнего хранилища приложения.
