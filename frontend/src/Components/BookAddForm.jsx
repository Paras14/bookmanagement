import React, { useEffect, useRef, useState } from "react";

function BookAddForm({showForm, setShowForm}) {

    const [invalidBookData, setInvalidBookData] = useState(false);

    const [newBookData, setNewBookData] = useState({isbn:'', bookName: '', authorName: '', genre: '', readStatus: false});
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

        const isbnRegex = /^\d{3}-\d{9}[\dX]$/;
        if (name === 'isbn' && !isbnRegex.test(value)) {
            setInvalidBookData(true);
            return;
        }

        if (name === 'bookName' && value.trim() === '') {
            setInvalidBookData(true);
            return;
        }

        if (name === 'authorName' && value.trim() === '') {
            setInvalidBookData(true);
            return;
        }

        if (name === 'genre' && value === '') {
            setInvalidBookData(true);
            return;
        }

        setNewBookData({...newBookData, [name]: value });
        setInvalidBookData(false);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(newBookData);
    };

  return (
    <div className={showForm ?"form-bg form-bg-visible" : "form-bg"}>
      <div className="add-book-form" ref={formRef}>
        <form onSubmit={handleSubmit}>
          {invalidBookData ? <p style={{color: 'red'}}>Invalid values!</p> : <></>}
          <label htmlFor="bookName">Book Name</label>
          <input type="text" name="bookName" value={newBookData.bookName} onChange={handleChange}/>
          <label htmlFor="author">Author Name</label>
          <input type="text" name="authorName" value={newBookData.authorName} onChange={handleChange}/>
          <label htmlFor="isbn">ISBN</label>
          <input type="text" name="isbn" value={newBookData.isbn} onChange={handleChange}/>
          <label htmlFor="genre">Genre</label>
          <select name="genre" id="genre" value={newBookData.genre} onChange={handleChange}>
            <option value="ROMANCE">Romance</option>
            <option value="COMEDY">Comedy</option>
            <option value="THRILLER">Thriller</option>
            <option value="SELF_HELP">Self Help</option>
            <option value="SCIENCE_FICTION">Science Fiction</option>
            <option value="MYSTERY">Mystery</option>
            <option value="FANTASY">Fantasy</option>
          </select>
          <br />
          <button type="submit" disabled={invalidBookData}>
            Submit
          </button>
        </form>
      </div>
    </div>
  );
}

export default BookAddForm;
