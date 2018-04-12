# fartman
old fartman in my namespace


Как запускать Scheduler Task в Java?
Иногда в Java нужно выполнить метод, через определенный интервал времени. Например, когда GUI приложение должно обновить некоторую информацию из базы данных, через определенный интервал времени.


Скачать
Шаг 1.
Создадим класс ScheduledTask.java и унаследуем от TimerTask:

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {

    @Override
    public void run() {

    }

}
как видите также мы переопределили метод run() он есть обязательным.

Шаг 2.
Давайте теперь реализуем наш scheduled task run:

package com.devcolibri;

import java.util.Date;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {

    Date now;

    // Добавляем такс
    @Override
    public void run() {
        now = new Date();
        System.out.println("Текущая дата и время : " + now);
    }

}
тут мы просто выводим текущее время по заданному времени.

Шаг 3.
Теперь проверим работу нашего ScheduleTask-а:

package com.devcolibri;

import java.util.Timer;

public class SchedulerMain {

    public static void main(String args[]) throws InterruptedException {

        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask();
        time.schedule(st, 0, 1000); // Создаем задачу с повторением через 1 сек.

        for (int i = 0; i <= 5; i++) {
            Thread.sleep(3000);  
            System.out.println("Execution in Main Thread. #" + i);
            if (i == 5) {
                System.out.println("Application Terminates");
                System.exit(0);
            }
        }
    }

}
В данном примере мы говорим журналу задач выполнять данный таск в интервале в 3 сек. в цикле с 5-ю повторениями.

Как вы заметили у нас есть Thread.sleep(3000); тут мы заставляем поток заснуть на 3-сек. это имитация ответа сервера при выполнении SQL запроса например.

И вот результат выполнения данного кода:

Текущая дата и время : Fri May 10 23:47:53 EEST 2013
Текущая дата и время : Fri May 10 23:47:54 EEST 2013
Текущая дата и время : Fri May 10 23:47:55 EEST 2013
Execution in Main Thread. #0
Текущая дата и время : Fri May 10 23:47:56 EEST 2013
Текущая дата и время : Fri May 10 23:47:57 EEST 2013
Текущая дата и время : Fri May 10 23:47:58 EEST 2013
Execution in Main Thread. #1
Текущая дата и время : Fri May 10 23:47:59 EEST 2013
Текущая дата и время : Fri May 10 23:48:00 EEST 2013
Текущая дата и время : Fri May 10 23:48:01 EEST 2013
Execution in Main Thread. #2
Текущая дата и время : Fri May 10 23:48:02 EEST 2013
Текущая дата и время : Fri May 10 23:48:03 EEST 2013
Текущая дата и время : Fri May 10 23:48:04 EEST 2013
Execution in Main Thread. #3
Текущая дата и время : Fri May 10 23:48:05 EEST 2013
Текущая дата и время : Fri May 10 23:48:06 EEST 2013
Текущая дата и время : Fri May 10 23:48:07 EEST 2013
Execution in Main Thread. #4
Текущая дата и время : Fri May 10 23:48:08 EEST 2013
Текущая дата и время : Fri May 10 23:48:09 EEST 2013
Текущая дата и время : Fri May 10 23:48:10 EEST 2013
Execution in Main Thread. #5
Application Terminates
 

 06/01/2018
2 комментариев к статье "Как запускать Scheduler Task в Java?"

ИГОРЬ18/07/2014 В 2:54 ППОТВЕТИТЬ
http://www.quartz-scheduler.org/overview – стандартная библиотека для реализации запланированных заданий. Есть поддержка cron триггеров.
Так же можно использовать для этих целей spring,

 
ИВАН04/04/2016 В 11:16 ППОТВЕТИТЬ
Есть же еще java.util.concurrent и никакие внешние библиотеки не нужны. Если конечно не ради cron триггеров подключать).