'use strict';
const navi = new Navigator("btn-prev-month","btn-next-month","btn-current-month");
// const store = memoryStore();
// const store = localStorageStore();
const store = apiStore();
console.log("year:" + navi.getYear());
console.log("month:" + navi.getMonth());

(function(){
    'use strict';

    window.addEventListener("DOMContentLoaded",(event)=>{
        displayYearMonth(navi.getYear(), navi.getMonth());
        createTodoList();
    });

    function displayYearMonth(targetYear, targetMonth){
        // html element : todo-nav-year 표시
        const todoNavYear = document.getElementById("todo-nav-year");
        todoNavYear.innerText = targetYear 
        // html element : todo-nav-month 표시
        const todoNavMonth = document.getElementById("todo-nav-month");
        todoNavMonth.innerText = navi.convertToZeroMonthAndDay(targetMonth);
    }

    function createTodoList(){
        const year = navi.getYear();
        const month = navi.getMonth();
    
        const daysInMonth = getDaysInMonth(year,month);
        console.log("daysInMonth:" + daysInMonth);
        const todoItemWrapper = document.getElementById("todo-item-container");
        const template = document.getElementById("todo-item-template");
    
        for(let i=1; i<=daysInMonth; i++ ){
           //https://developer.mozilla.org/ko/docs/Web/API/Document/importNode
            const todoItem = document.importNode(template.content,true);
            const todoItemDay = todoItem.querySelector(".todo-item-day");
    
            //날짜표시
            todoItemDay.innerText="";
            const span1 = document.createElement("span");
            span1.innerText=i;
            todoItemDay.appendChild(span1);
    
            //form 날짜 설정 name=date
            //https://developer.mozilla.org/ko/docs/Web/API/Document/querySelector
            const todoDate = todoItem.querySelector("input[name=todoDate]");
            todoDate.value=year + "-" + navi.convertToZeroMonthAndDay(month) + "-" + navi.convertToZeroMonthAndDay(i);
            // console.log(todoDate.value);
            //form 전송 event
            const form = todoItem.querySelector("form");
            form.addEventListener("submit",todoSubmit);
    
            const todoItemList = todoItem.querySelector(".todo-item-list");
            todoItemList.setAttribute("id","todo-item-list-"+todoDate.value);
            //TODO#1 - 구현 .. 
            const btnRemoveAll = todoItem.querySelector(".btn-remove-all");
            btnRemoveAll.setAttribute("todoDate",todoDate.value);
            btnRemoveAll.addEventListener("click", removeAllByTodoDate);
    
            todoItemWrapper.appendChild(todoItem);
            displayToDoList(todoDate.value);
        }
    }
    
    //ex) 2023-02 = 28일 , 2023-03 = 31 .. 해당달의 day count 구하기
    function getDaysInMonth(targetYear, targetMonth){
        return new Date(targetYear, parseInt(targetMonth), 0).getDate();
    }
    
    // form event 처리
    async function todoSubmit(event){
        //TODO#2 - form 이벤트 구현
        event.preventDefault();
        const validateForm =(form)=>{
            const date = form['todoDate'].value;
            const content = form['todoSubject'].value;
            if(date==0){
                alert("date empty");
                return false;
            }
            if(content==0){
                alert("할일을 적어주세요");
                return false;
            }
            console.log(date +","+content);
            return true;
        }
    
        if(validateForm(event.target)) {
          
            const todoDate = event.target['todoDate'].value;
            const todoSubject = event.target['todoSubject'].value;
        
            try{
              await store.save(todoDate,todoSubject);
            }catch(e){
                console.log(e);
            }finally{
                event.target['todoSubject'].value="";
                displayToDoList(todoDate);
            }
        }
    
    }
    
    async function removeAllByTodoDate(event){
        const todoDate  = event.target.getAttribute("todoDate");
        const answer =confirm("모두삭제 하시겠습니까?");

        if(answer){
            try{
               await store.deleteByTodoDate(todoDate);
            }catch(e){
                alert(e);
            }finally{
                clearTodoItemList(todoDate);
            }
        }
    }

    //ex) 2023-02 = 28일 , 2023-03 = 31 .. 해당달의 day count 구하기
    function getDaysInMonth(targetYear, targetMonth){
        return new Date(targetYear, parseInt(targetMonth), 0).getDate();
    }

    //clear id = todo-item-list-{todoDate}
    function clearTodoItemList(todoDate){
        const todoItemList = document.getElementById("todo-item-list-" + todoDate);
        while(todoItemList.firstChild){
            todoItemList.removeChild(todoItemList.firstChild)
        }
    }
    
    async function displayToDoList(todoDate) {
        clearTodoItemList(todoDate);

        const todoItemList = document.getElementById("todo-item-list-"+todoDate);
        try{
            const items = await store.getTodoItemList(todoDate);
            // console.log(items);
            for(let i=0;i<items.length;i++){
                const li = document.createElement("li");
                console.log( items[i].todoSubject);
                li.innerText = items[i].todoSubject;
                li.setAttribute("todoDate",todoDate);
                li.setAttribute("id", items[i].id);
                li.addEventListener("click", removeTodoItem);
                todoItemList.appendChild(li);
            }
        }catch(e){
            alert(e);
        }
    }

    async function removeTodoItem(event){
        const id = event.target.getAttribute("id");
        const todoDate = event.target.getAttribute("todoDate");
        const answer = confirm("삭제하시겠습니까?");

        if(answer){
            try{
               await store.delete(todoDate,id);
            }catch(e){
                alert(e);
            }finally{
                displayToDoList(todoDate);
            }
        }
    }


})();



