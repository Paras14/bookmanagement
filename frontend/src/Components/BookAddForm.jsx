import React, { useEffect, useRef, useState } from "react";

function BookAddForm({showForm, setShowForm}) {

    const [newBookData, setNewBookData] = useState({bookName: '', authorName: '', genre: ''});
    const formRef = useRef(null);

    useEffect(() => {
      const handleClickOutside = (e) => {
        if(formRef.current && !formRef.current.contains(e.target)){
          setShowForm(false);
        }
      }

      document.addEventListener("mousedown", handleClickOutside);

      return () => {
        document.removeEventListener("mousedown", handleClickOutside);
      }

    }, [formRef, setShowForm])

    const handleChange = (e) => {
        const {name, value} = e.target;
        setNewBookData({...newBookData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(newBookData);
    };

  return (
    <div className={showForm ?"form-bg form-bg-visible" : "form-bg"}>
      <div className="add-book-form" ref={formRef}>
        <form onSubmit={handleSubmit}>
          <label htmlFor="bookName">Book Name</label>
          <input type="text" name="bookName" value={newBookData.bookName} onChange={handleChange}/>
          <label htmlFor="author">Author Name</label>
          <input type="text" name="authorName" value={newBookData.authorName} onChange={handleChange}/>
          <label htmlFor="genre">Genre</label>
          <select name="genre" id="genre" value={newBookData.genre} onChange={handleChange}>
            <option value="horror">Horror</option>
            <option value="comedy">Comedy</option>
            <option value="thriller">Thriller</option>
            <option value="self-help">Self Help</option>
            <option value="biography">Biography</option>
          </select>
          <br />
          <button type="submit">
            Submit
          </button>
        </form>
      </div>
    </div>
  );
}

export default BookAddForm;
