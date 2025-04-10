import React, { useState, useEffect } from "react";
import { request } from "../helpers/axios_helper";
import './todoList.css'

function TodoList(props) {

  const [state, setState] = useState("fetching");
  // states: fetching, failed, finished
  const [todoLists, setTodoLists] = useState([]);
  const [currentPage, setCurrentpage] = useState(0);
  const [pageSize, setPageSize] = useState(5);

  function getAllTodoList() {
    setState("fetching");
    // request(
    //   "GET",
    //   "/todo-list",
    //   {
    //     page: currentPage,
    //     size: pageSize
    //   }).then(
    //     (response) => {
    //       setState("finished");
    //       setTodoLists(response.data.content);
    //       sortListData();
    //     }).catch(
    //       (error) => {
    //         onRequestFailed(error);
    //         setState("failed");
    //       }
    //     );
    setTestData();
  }

  function setTestData() {

    setTodoLists([{
      id: 1,
      title: "My todo list 3",
      todoListItems: [
        {
          id: 3,
          orderInList: 0,
          description: "Study English",
          status: "planned",
          isDeleted: false
        }, {
          id: 4,
          orderInList: 1,
          description: "Make essay",
          status: "planned",
          isDeleted: false
        }],
      isDeleted: false
    },
    {
      id: 2,
      title: "My todo list 5",
      todoListItems: [
        {
          id: 5,
          orderInList: 2,
          description: "Study English",
          status: "planned",
          isDeleted: false
        }, {
          id: 6,
          orderInList: 0,
          description: "Make essay",
          status: "planned",
          isDeleted: false
        }],
      isDeleted: false
    }], sortListData());

  }

  function sortListData() {
    let updatedLists = [...todoLists];
    updatedLists.sort((a, b) => a.id > b.id ? 1 : -1)
    updatedLists.map(list => {
      list.todoListItems.sort((a, b) => a.orderInList > b.orderInList ? 1 : -1);
    })
    setTodoLists(updatedLists);
    setState("finished");
  }

  function onRequestFailed(error) {
    if (error.status === 401) {
      setAuthenticated(false);
    }
    console.log("Error occured!" + error);
  }

  function onRequestTodoListSuccessfully() {

  }

  function onRequestAddNewTodoList() {

  }

  function handleDeleteList(list) {
    const updatedTodoLists = [...todoLists];
    const listIndex = updatedTodoLists.indexOf(list);
    const targetList = updatedTodoLists[listIndex];
    targetList.isDeleted = true;
    setTodoLists(updatedTodoLists);
  }

  function handleDeleteItem(list, item) {
    const updatedTodoLists = [...todoLists];
    const listIndex = updatedTodoLists.indexOf(list);
    const targetList = updatedTodoLists[listIndex];
    const index = targetList.todoListItems.indexOf(item);
    targetList.todoListItems[index].isDeleted = true;
    setTodoLists(updatedTodoLists);
  }



  useEffect(() => {
    getAllTodoList();
  }, [])

  function moveItemUp(todoList, index) {
    if (index > 0) {
      const updatedTodoLists = [...todoLists];
      const listIndex = updatedTodoLists.indexOf(todoList);
      const targetList = updatedTodoLists[listIndex];

      // const temp = targetList.todoListItems[index].orderInList;
      // targetList.todoListItems[index].orderInList = targetList.todoListItems[index - 1].orderInList;
      // targetList.todoListItems[index - 1].orderInList = temp;
      [targetList.todoListItems[index].orderInList, targetList.todoListItems[index - 1].orderInList] = 
      [targetList.todoListItems[index - 1].orderInList, targetList.todoListItems[index].orderInList];
      setTodoLists(updatedTodoLists, sortListData());
    }
  }



  if (state === "fetching") {
    return (
      <div>
        <img className="loading-image" src="src\assets\loading_icon.svg"></img>
      </div>
    );
  }

  if (state === "failed") {
    return (
      <div>
        <p>Failed to load data</p>
        <button onClick={getAllTodoList} className="control-button">Try again</button>
      </div>
    )
  }

  //console.log(todoLists);
  return (
    <>
      <h1>Todo list</h1>
      <button onClick={() => getAllTodoList()}>Refresh</button>
      <div className="display-box">
        <ul className="todo-list">
          {todoLists !== null && todoLists.map(list =>
          (list.isDeleted === false && <li key={list.id}>
            <div className="todo-list-row">
              <h2> {list.title}</h2>
              <div className="todo-list-item-control">
                <button className="control-button todo-list-delete" onClick={() => handleDeleteList(list)}> X
                </button>
              </div>
            </div>
            <ul className="todo-list-items">
              {list.todoListItems !== null && list.todoListItems.map((item, index) =>
              (item.isDeleted === false && <li key={index} className="">
                <div className="todo-list-item-order">{index}</div>
                <div className="todo-list-item-des">{item.description}</div>
                <div className="todo-list-item-control">
                  <button onClick={() => moveItemUp(list, index)} className="control-button item-move-up">Up</button>
                  <button onClick={() => moveItemDown(list, index)} className="control-button item-move-down">Down</button>
                  <button onClick={() => handleDeleteItem(list, item)} className="control-button item-delete"> X </button>
                </div>

              </li>))
              }
            </ul>
          </li>)
          )}

        </ul>
      </div>
    </>
  )
}

export default TodoList

function TodoListItemRow(item) {
  console.log(item);
  return (
    <>
    </>
  );
}